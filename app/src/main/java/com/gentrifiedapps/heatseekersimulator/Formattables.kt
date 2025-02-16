package com.gentrifiedapps.heatseekersimulator

import com.gentrifiedapps.heatseekersimulator.GlobalVals.Companion.inToPixels
import com.gentrifiedapps.heatseekersimulator.GlobalVals.Companion.waypointRad
import javafx.scene.control.TextArea
import org.gentrifiedApps.gentrifiedAppsUtil.classes.Point
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Waypoint
import kotlin.math.abs

class Formattables {
    fun formatString(textField:TextArea): MutableList<Waypoint> {
        val waypoints = mutableListOf<Waypoint>()
        if (textField.text.isNotEmpty()) {
            textField.text.split(",").forEach { line ->
                line.strip()
                val coordinates = line.split(" ")
                if (coordinates.size == 2) {
                    val x = coordinates[0].toDoubleOrNull()
                    val y = coordinates[1].toDoubleOrNull()
                    if (x != null && y != null) {
                        waypoints.add(Waypoint(x * inToPixels,y * inToPixels,Math.toRadians(90.0),1.0))
                    }
                }
            }
        }
        return waypoints
    }

    fun wrapWaypointToPix(waypoint: Waypoint):Waypoint{
        // wrap a -72 to 72 grid to a 0 to 144 grid
        val x = if (waypoint.x <= 0) {
            (waypoint.x)+72* inToPixels
        } else if (waypoint.x > 0) {
           waypoint.x +72* inToPixels
        } else {
            waypoint.x
        }
        val y = if (waypoint.y >= 0) {
            72* inToPixels-(waypoint.y)
        } else if (waypoint.y < 0) {
            abs(waypoint.y) +72* inToPixels
        }else{
            waypoint.y
        }
        return Waypoint(x,y,waypoint.h,waypoint.velocity)
    }
    fun wrapPixToWay(waypoint: Waypoint):Waypoint{
        // wrap a -72 to 72 grid to a 0 to imageParam grid
        val xIn = waypoint.x * 1/inToPixels
        val yIn = waypoint.y  * 1/inToPixels
        val x = if (xIn <= 0) {
            (xIn)+72
        } else if (xIn > 0) {
            xIn -72
        } else {
            xIn
        }
        val y = if (yIn >= 0) {
            72-yIn
        } else if (yIn < 0) {
            abs(yIn) -72
        }else{
            yIn
        }

        return Waypoint(x,y,waypoint.h,waypoint.velocity)
    }
}