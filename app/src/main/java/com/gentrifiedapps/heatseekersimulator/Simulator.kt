package com.gentrifiedapps.heatseekersimulator

import com.gentrifiedapps.heatseekersimulator.GlobalVals.Companion.centerWaypoint
import com.gentrifiedapps.heatseekersimulator.GlobalVals.Companion.isSimulating
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Waypoint

class Simulator(val robot: Robot, val canvas: Canvas, val gc: GraphicsContext) {
    var currentWaypoint = centerWaypoint
    fun reset(){
        robot.setPos(currentWaypoint)
        robot.angle = centerWaypoint.h
    }

    fun update(pubwaypoints: MutableList<Waypoint>) {
        if (isSimulating) {
            if (pubwaypoints.isNotEmpty()) {
                if (robot.isAt(currentWaypoint,2.0)) {
                    val currentIndex = pubwaypoints.indexOf(currentWaypoint)
                    if (currentIndex < pubwaypoints.size - 1) {
                        currentWaypoint = pubwaypoints[currentIndex + 1]
                    }
                }
//                else if (robot.isAt(currentWaypoint, 2.0)) {
//                    robot.setPos(currentWaypoint)
//                }
                robot.moveTo(currentWaypoint)
            }
        } else {
            if (pubwaypoints.isNotEmpty()) {
                val firstPose = pubwaypoints[0]
                robot.setPos(firstPose)
                if (pubwaypoints.size > 1) {
                    currentWaypoint = pubwaypoints[1]
                }
            } else {
                robot.setPos(centerWaypoint)
            }
        }
        robot.draw(gc)
    }
}