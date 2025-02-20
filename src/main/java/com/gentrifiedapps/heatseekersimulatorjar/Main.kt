package com.gentrifiedapps.heatseekersimulatorjar

import javafx.application.Application

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        Application.launch(RoboticsSimulator::class.java, *args)
    }
}
