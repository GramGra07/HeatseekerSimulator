package com.gentrifiedapps.heatseekersimulator.util

import com.gentrifiedapps.heatseekersimulator.Vals.GlobalVals.Companion.angleMult
import com.gentrifiedapps.heatseekersimulator.Vals.GlobalVals.Companion.width
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.pointClasses.Waypoint

class MathFunctions {
    companion object {
        fun mpsTommps(mps: Double): Double {
            return mps * 1000
        }

        fun mmpsTomps(mmps: Double): Double {
            return mmps / 1000
        }

        fun mmpsToinps(mmps: Double): Double {
            return mmps / 25.4
        }

        fun rpmToRps(rpm: Double): Double {
            return rpm / 60
        }

        fun applyGearRatio(rpm: Double, gearRatio: Double): Double {
            return rpm / gearRatio
        }

        fun rpmTommps(rpm: Double, wheelDiameter: Double): Double {
            return rpmToRps(rpm) * (wheelDiameter * Math.PI)
        }

        fun mmToIn(mm: Double): Double {
            return mm / 25.4
        }

        fun inpsToinpf(inps: Double, fps: Double): Double {
            return inps / fps
        }

        fun inToMeters(inches: Double): Double {
            return inches * 0.0254
        }

        fun Waypoint.distanceTo(other: Waypoint): Double {
            val dx = this.x - other.x
            val dy = this.y - other.y
            return Math.sqrt(dx * dx + dy * dy)
        }

        fun mpsToamps(mps: Double): Double {
            return (mps / (width / 2)) * angleMult
        }
    }
}