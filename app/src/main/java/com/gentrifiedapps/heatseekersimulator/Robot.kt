package com.gentrifiedapps.heatseekersimulator

import com.gentrifiedapps.heatseekersimulator.GlobalVals.Companion.inToPixels
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color.BLUE
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Waypoint
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Robot {
    var x = 300.0
    var y = 300.0
    var robotWidth = 18
    var robotHeight = 18
    var maxSpeed = 1.2
    var angle = Math.toRadians(90.0)
    var velocity = 1.0

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
       gc.rotate(Math.toDegrees(angle))

       // Draw the robot rectangle centered at the origin
       gc.fillRect(-inToPixels * robotHeight / 2, -inToPixels * robotWidth / 2, inToPixels * robotHeight, inToPixels * robotWidth)

       // Restore the previous state of the GraphicsContext
       gc.restore()

       gc.globalAlpha = 0.9
       gc.stroke = BLUE
       gc.lineWidth = 4.0

       // Calculate the heading line based on the angle
       val headingLength = -0.5 * inToPixels * robotHeight + 2 // Length of the heading line
       val lineEndX = centerX + cos(angle) * headingLength
       val lineEndY = centerY + sin(angle) * headingLength

       // Draw the heading line
       gc.strokeLine(centerX, centerY, lineEndX, lineEndY)
   }

    // Move towards a target based on position and angle error
    fun moveTo(target: Waypoint, canvasWidth: Double, canvasHeight: Double) {
        this.velocity = target.velocity*maxSpeed

        val halfWidth = (inToPixels * robotWidth) / 2
        val halfHeight = (inToPixels * robotHeight) / 2

        // Calculate the target position relative to the robot's center
        val targetX = target.x-(inToPixels*robotWidth)/2
        val targetY = target.y-(inToPixels*robotHeight)/2

        // Compute direction
        val dx = targetX - x
        val dy = targetY - y
        val distance = sqrt(dx * dx + dy * dy)

        // Calculate the angle to the target
        val angleToTarget = atan2(dy, dx)

        // Calculate the angle error
        val angleError = target.h - angle
        if (distance > 2) {
            val angleProp = angleError / distance

            angle += angleProp
        }

        val moveDistance = minOf(distance, velocity)
        x += moveDistance * cos(angleToTarget)
        y += moveDistance * sin(angleToTarget)

        // Clamp to prevent going off-screen
        x = x.coerceIn(halfWidth, canvasWidth - halfWidth)
        y = y.coerceIn(halfHeight, canvasHeight - halfHeight)

        // Update angle to always face the waypoint
//        angle = angleToTarget
    }

    fun isAt(waypoint: Waypoint,t: Double = 0.1): Boolean {
        val tolerance = t * inToPixels // Adjust the tolerance as needed
        val centerX = x + (inToPixels * robotWidth) / 2
        val centerY = y + (inToPixels * robotHeight) / 2
        return abs(centerX - waypoint.x) < tolerance && abs(centerY - waypoint.y) < tolerance
    }

    fun setPos(waypoint: Waypoint){
        this.x = waypoint.x-(inToPixels*robotWidth)/2
        this.y = waypoint.y-(inToPixels*robotHeight)/2
        this.angle = waypoint.h
    }

}