package com.gentrifiedapps.heatseekersimulatorjar.util

import com.gentrifiedapps.heatseekersimulatorjar.vals.GlobalVals.Companion.inToPixels
import javafx.scene.control.TextArea
import kotlin.math.abs

class Formattables {
    //data class Waypoint(val target2D: Target2D, val velocity: Double) {
    //    constructor(x: Double, y: Double, h: Double, velocity: Double) : this(
    //        Target2D(
    //            x,
    //            y,
    //            Angle(h, AngleUnit.RADIANS)
    //        ), velocity
    //    )
    //
    //    constructor(x: Double, y: Double, h: Angle, velocity: Double) : this(
    //        Target2D(x, y, h),
    //        velocity
    //    )

    //.addWaypoint(new Waypoint(new Target2D(0, 10.0, new Angle(0, AngleUnit.RADIANS)), 0.5))
    //    .addWaypoint(new Waypoint(new Target2D(0, 10.0, new Angle(0, AngleUnit.RADIANS)), 0.5))
    enum class WaypointType {
        WAYPOINTd, WAYPOINT, WAYPOINTONLYAngle
    }

    fun identifySignature(line: String): WaypointType {
        //List<Waypoint> waypoints = new PathBuilder()
        //                .addWaypoint(new Waypoint(new Target2D(0, 10.0, new Angle(0, AngleUnit.RADIANS)), 0.5))
        //                .build();
        return if (line.contains("Target2D") && line.contains("Angle")) {
            WaypointType.WAYPOINT
        } else if (line.contains("Angle")) {
            WaypointType.WAYPOINTONLYAngle
        } else {
            WaypointType.WAYPOINTd
        }
    }

    fun readLineCorrected(line: String, signature: WaypointType): Waypoint? {
        val correctedLine = line.replace("Waypoint(", "").replace("new", "").replace("new ", "")
        if (line.replace("\n", "").isEmpty()) {
            return null
        }
        return when (signature) {
            WaypointType.WAYPOINT -> {
                //.addWaypoint(new Waypoint(new Target2D(0, 10.0, new Angle(0, AngleUnit.RADIANS)), 0.5))
                val x = correctedLine.substringAfter("Target2D(").substringBefore(",").toDouble()
                val linecorrected = correctedLine.removeRange(0, correctedLine.indexOf(",") + 1)
                val y = linecorrected.substringBefore(",").toDouble()
                val linecorrected2 = linecorrected.removeRange(0, linecorrected.indexOf(",") + 1)
                var h = linecorrected2.substringAfter("Angle(").substringBefore(",").toDouble()
                val linecorrected3 = linecorrected2.removeRange(0, linecorrected2.indexOf(",") + 1)
                val rad = linecorrected3.substringBefore(")),")
                if (rad.contains("DEGREES")) {
                    h = Math.toRadians(h)
                }
                val linecorrected4 = linecorrected3.removeRange(0, linecorrected3.indexOf(")") + 1)
                val velocity = linecorrected4.substringAfter(",").substringBefore(")").toDouble()
                //TODO check velo cant be more than 1
                Waypoint(x * inToPixels, y * inToPixels, h, velocity)
            }

            WaypointType.WAYPOINTONLYAngle -> {
                //.addWaypoint(new Waypoint(0, 10.0, new Angle(0, AngleUnit.RADIANS), 0.5))
                val line2 = correctedLine.removePrefix(".add").replace(")", "")
                val split = line2.split(",")
                val x = split[0].toDouble()
                val y = split[1].toDouble()
                val angle = split[2].substringAfter("Angle(").substringBefore(",").toDouble()
                val rad = split[3]
                val h = if (rad.contains("DEGREES")) {
                    Math.toRadians(angle)
                } else {
                    angle
                }
                Waypoint(x * inToPixels, y * inToPixels, h, split[4].toDouble())
            }

            WaypointType.WAYPOINTd -> {
                val line2 = correctedLine.removePrefix(".add").replace(")", "")
                val split = line2.split(",")
                val x = split[0].toDouble()
                val y = split[1].toDouble()
                val h = Math.toRadians(split[2].toDouble())
                val velocity = split[3].toDouble()
                // .addWaypoint(new Waypoint(0, 10.0, 0, 0.5))
                Waypoint(x * inToPixels, y * inToPixels, h, velocity)
            }
        }
    }

    fun formatString(textField: TextArea): MutableList<Waypoint> {
        val waypoints = mutableListOf<Waypoint>()
        if (textField.text.isNotEmpty()) {
            textField.text.split("\n").forEach { line ->
                line.strip()
                try {
                    val signature = identifySignature(line)
                    val line = readLineCorrected(line, signature)
                    if (line != null) {
                        waypoints.add(line)
                    }
                } catch (e: Exception) {
//                    println("Error in line: $line")
                }
            }
        }
        return waypoints
    }

    fun wrapWaypointToPix(waypoint: Waypoint): Waypoint {
        // wrap a -72 to 72 grid to a 0 to 144 grid
        val x = if (waypoint.x <= 0) {
            (waypoint.x) + 72 * inToPixels
        } else if (waypoint.x > 0) {
            waypoint.x + 72 * inToPixels
        } else {
            waypoint.x
        }
        val y = if (waypoint.y >= 0) {
            72 * inToPixels - (waypoint.y)
        } else if (waypoint.y < 0) {
            abs(waypoint.y) + 72 * inToPixels
        } else {
            waypoint.y
        }
        return Waypoint(x, y, waypoint.h, waypoint.velocity)
    }

    fun wrapPixToWay(waypoint: Waypoint): Waypoint {
        // wrap a -72 to 72 grid to a 0 to imageParam grid
        val xIn = waypoint.x * 1 / inToPixels
        val yIn = waypoint.y * 1 / inToPixels
        val x = if (xIn <= 0) {
            (xIn) + 72
        } else if (xIn > 0) {
            xIn - 72
        } else {
            xIn
        }
        val y = if (yIn >= 0) {
            72 - yIn
        } else if (yIn < 0) {
            abs(yIn) - 72
        } else {
            yIn
        }

        return Waypoint(x, y, waypoint.h, waypoint.velocity)
    }
}