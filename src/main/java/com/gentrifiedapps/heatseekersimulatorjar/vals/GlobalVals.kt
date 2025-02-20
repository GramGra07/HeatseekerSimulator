package com.gentrifiedapps.heatseekersimulatorjar.vals

import com.gentrifiedapps.heatseekersimulatorjar.util.Waypoint

class GlobalVals {
    companion object {
        const val width = 1200.0
        const val height = 650.0
        var isSimulating = false
        var toolbarHeight = 50.0

        var imageParam = width / 2 - toolbarHeight
        var inToPixels = imageParam / 144
        var fps = 60.0


        val angleMult = inToPixels * 2

        val centerWaypoint = Waypoint(imageParam / 2, imageParam / 2, Math.toRadians(90.0), 0.0)

        val waypointRad = 5.0
    }
}