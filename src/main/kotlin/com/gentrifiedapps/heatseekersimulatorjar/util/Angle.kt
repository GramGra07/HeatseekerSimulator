package com.gentrifiedapps.heatseekersimulatorjar.util

data class Angle(val angle: Double, val unit: AngleUnit = AngleUnit.RADIANS) {
    fun toRadians(): Double {
        return when (unit) {
            AngleUnit.DEGREES -> Math.toRadians(angle)
            AngleUnit.RADIANS -> angle
        }
    }

    fun toDegrees(): Double {
        return when (unit) {
            AngleUnit.DEGREES -> angle
            AngleUnit.RADIANS -> Math.toDegrees(angle)
        }
    }

    fun toAngleUnit(unit: AngleUnit): Angle {
        return when (unit) {
            AngleUnit.DEGREES -> Angle(toDegrees(), AngleUnit.DEGREES)
            AngleUnit.RADIANS -> Angle(toRadians(), AngleUnit.RADIANS)
        }
    }

    fun angleWrap(wrap: Double = 360.0): Angle {
        return Angle((this.toDegrees() % wrap + wrap) % wrap, AngleUnit.DEGREES).toAngleUnit(unit)
    }

    fun errorTo(target: Angle): Angle {
        val unit = target.unit
        val t =
            if (target.angle > 360 || target.angle < 0) target.angleWrap().angle else target.angle
        val a = if (angle > 360 || angle < 0) angleWrap().angle else angle
        val error = t - a
        return Angle(error, unit)
    }

    fun errorToD(target: Angle): Double {
        return errorTo(target).angle
    }

    companion object {
        fun blank(): Angle {
            return Angle(0.0, AngleUnit.DEGREES)
        }
    }
}
