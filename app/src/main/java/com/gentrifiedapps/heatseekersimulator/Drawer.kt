package com.gentrifiedapps.heatseekersimulator

import com.gentrifiedapps.heatseekersimulator.GlobalVals.Companion.isSimulating
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.control.ToolBar
import javafx.scene.text.Text
import javafx.stage.Stage

class ToolbarDrawer(val robot: Robot, val simulator: Simulator,val stage: Stage) {
    val toolbar = ToolBar()
    val runButton = Button("Simulate")
    val stopButton = Button("Reset simulation")
    val wid = "Robot Width"
    val text = Text(wid)
    val inputField = TextField(robot.robotWidth.toString())
    val height = "Robot Height"
    val text2 = Text(height)
    val inputField2 = TextField(robot   .robotHeight.toString())

    val maxSpeed = "Calculate speed"
    val maxTxt = Button(maxSpeed)
    init {
        text.style = "-fx-fill: #ffffff;"
        text2.style = "-fx-fill: #ffffff;"
        toolbar.items.addAll(runButton, stopButton, text,inputField,text2,inputField2,maxTxt)
        toolbar.style = "-fx-background-color: #2b2b2b; -fx-text-fill: #ffffff;"
        maxTxt.setOnAction {
            SpeedCalculatorWindow(robot).start(stage)
        }
        inputField.setOnKeyReleased {
            if (inputField.text.isNotEmpty()) {
                if (inputField.text.toInt() > 18) {
                    inputField.text = "18"
                }
                robot.robotWidth = inputField.text.toInt()
                FW().writeHeightWidthToCSV(listOf( robot.robotHeight, robot.robotWidth))
            }
        }
        inputField2.setOnKeyReleased {
            if (inputField2.text.isNotEmpty()) {
                if (inputField2.text.toInt() > 18) {
                    inputField2.text = "18"
                }
                robot.robotHeight = inputField2.text.toInt()
                FW().writeHeightWidthToCSV(listOf( robot.robotHeight, robot.robotWidth))
            }
        }
        runButton.setOnAction {
            isSimulating = true
        }
        stopButton.setOnAction {
            isSimulating = false
            simulator.reset()
        }
    }
}