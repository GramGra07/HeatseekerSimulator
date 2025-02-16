package com.gentrifiedapps.heatseekersimulator

import com.gentrifiedapps.heatseekersimulator.GlobalVals.Companion.isSimulating
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.control.ToolBar
import javafx.scene.text.Text

class ToolbarDrawer(val robot: Robot, val simulator: Simulator) {
    val toolbar = ToolBar()
    val runButton = Button("Simulate")
    val stopButton = Button("Reset simulation")
    val wid = "Robot Width"
    val text = Text(wid)
    val inputField = TextField(robot.robotWidth.toString())
    val height = "Robot Height"
    val text2 = Text(height)
    val inputField2 = TextField(robot   .robotHeight.toString())

    val maxSpeed = "Max speed"
    val maxTxt = Text(maxSpeed)
    val maxTxtField = TextField(robot.maxSpeed.toString())
    init {
        toolbar.items.addAll(runButton, stopButton, text,inputField,text2,inputField2,maxTxt,maxTxtField)
        maxTxtField.setOnKeyReleased {
            if (maxTxtField.text.isNotEmpty()) {
                robot.maxSpeed = maxTxtField.text.toDouble()
            }
        }
        inputField.setOnKeyReleased {
            if (inputField.text.isNotEmpty()) {
                if (inputField.text.toInt() > 18) {
                    inputField.text = "18"
                }
                robot.robotWidth = inputField.text.toInt()
            }
        }
        inputField2.setOnKeyReleased {
            if (inputField2.text.isNotEmpty()) {
                if (inputField2.text.toInt() > 18) {
                    inputField2.text = "18"
                }
                robot.robotHeight = inputField2.text.toInt()
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