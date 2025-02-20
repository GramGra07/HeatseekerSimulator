package com.gentrifiedapps.heatseekersimulatorjar

import com.gentrifiedapps.heatseekersimulatorjar.vals.GlobalVals.Companion.inToPixels
import com.gentrifiedapps.heatseekersimulatorjar.drawers.PIDControllerView
import com.gentrifiedapps.heatseekersimulatorjar.util.FW
import com.gentrifiedapps.heatseekersimulatorjar.util.MathFunctions.Companion.inpsToinpf
import com.gentrifiedapps.heatseekersimulatorjar.util.MathFunctions.Companion.mmpsToinps
import com.gentrifiedapps.heatseekersimulatorjar.util.MathFunctions.Companion.mpsToamps
import com.gentrifiedapps.heatseekersimulatorjar.util.MathFunctions.Companion.mpsTommps
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color.BLUE
import com.gentrifiedapps.heatseekersimulatorjar.util.Waypoint
import kotlin.math.abs

class Robot {
    private var x = 300.0
    private var y = 300.0
    var robotWidth = 18
    var robotHeight = 18
    var angle = Math.toRadians(90.0)

    fun currentWaypoint(): Waypoint {
        return Waypoint(x, y, angle, 0.0)
    }


    val pidControllerViewX = PIDControllerView()
    val pidControllerViewY = PIDControllerView()
    val pidControllerViewH = PIDControllerView()

    // speed
    var mps = 1.2
    private var mmps = mpsTommps(mps)
    private var inps = mmpsToinps(mmps)
    private var inpf = inpsToinpf(inps, 1.0)
    private var angularVelocity = mpsToamps(mps)

    fun calculateSpeeds(fps: Double) {
        mmps = mpsTommps(mps)
        inps = mmpsToinps(mmps)
        inpf = inpsToinpf(inps, fps)
        angularVelocity = mpsToamps(mps)
    }

    init {
        val hw = FW().readHeightWidthFromCSV()
        robotHeight = hw[0].toInt()
        robotWidth = hw[1].toInt()
        if (robotHeight < 0) {
            robotHeight = 1
        } else if (robotHeight > 18) {
            robotHeight = 18
        }
        if (robotWidth < 0) {
            robotWidth = 1
        } else if (robotWidth > 18) {
            robotWidth = 18
        }
        calculateSpeeds(60.0)
    }

    fun draw(gc: GraphicsContext) {
        gc.fill = BLUE
        gc.globalAlpha = 0.6

        // Calculate the center of the robot
        val centerX = x + inToPixels * robotWidth / 2
        val centerY = y + inToPixels * robotHeight / 2

        // Save the current state of the GraphicsContext
        gc.save()

        // Translate to the center of the robot
        gc.translate(centerX, centerY)

        // Rotate the GraphicsContext around the center of the robot
        gc.rotate(-Math.toDegrees(angle))

        // Draw the robot rectangle centered at the origin
        gc.fillRect(
            -inToPixels * robotHeight / 2,
            -inToPixels * robotWidth / 2,
            inToPixels * robotHeight,
            inToPixels * robotWidth
        )

        // Draw the heading line within the rotated coordinate space
        gc.globalAlpha = 0.9
        gc.stroke = BLUE
        gc.lineWidth = 4.0

        val headingLength = (0.5 * inToPixels * robotHeight) - 4 // Length of the heading line

        // The line starts at the center and extends outward
        gc.strokeLine(0.0, 0.0, headingLength, 0.0)

        // Restore the previous state of the GraphicsContext
        gc.restore()
        gc.globalAlpha = 1.0
    }


    fun moveTo(target: Waypoint) {
        val halfWidth = (inToPixels * robotWidth) / 2
        val halfHeight = (inToPixels * robotHeight) / 2

        // Target position relative to robot's center
        val targetX = target.x - halfWidth
        val targetY = target.y - halfHeight

        // PID control for position
        val xCorrection = (pidControllerViewX.getPID()
            .calculatePID(targetX, x) / pidControllerViewX.getMax()) * target.velocity * (inpf)

        val yCorrection = (pidControllerViewY.getPID()
            .calculatePID(targetY, y) / pidControllerViewY.getMax()) * target.velocity * (inpf)

        // PID control for angle
        val angleCorrection = pidControllerViewH.getPID()
            .calculatePID(target.h, angle)
            .coerceIn(-angularVelocity, angularVelocity)

        // Adjust angle smoothly
        angle += angleCorrection

        // Move only using PID correction (do not use moveDistance)
        x += xCorrection
        y += yCorrection
    }


    fun isAt(waypoint: Waypoint, t: Double = 0.2): Boolean {
        val tolerance = t * inToPixels // Adjust the tolerance as needed
        val centerX = x + (inToPixels * robotWidth) / 2
        val centerY = y + (inToPixels * robotHeight) / 2
        return abs(centerX - waypoint.x) < tolerance && abs(centerY - waypoint.y) < tolerance
    }

    fun setPos(waypoint: Waypoint) {
        this.x = waypoint.x - (inToPixels * robotWidth) / 2
        this.y = waypoint.y - (inToPixels * robotHeight) / 2
        this.angle = waypoint.h
    }

}