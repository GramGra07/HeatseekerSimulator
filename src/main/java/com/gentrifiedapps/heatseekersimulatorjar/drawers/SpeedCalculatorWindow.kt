package com.gentrifiedapps.heatseekersimulator.drawers

import com.gentrifiedapps.heatseekersimulatorjar.Robot
import com.gentrifiedapps.heatseekersimulatorjar.GlobalVals.Companion.fps
import com.gentrifiedapps.heatseekersimulatorjar.util.FW
import com.gentrifiedapps.heatseekersimulatorjar.util.MathFunctions.Companion.applyGearRatio
import com.gentrifiedapps.heatseekersimulatorjar.util.MathFunctions.Companion.mmpsTomps
import com.gentrifiedapps.heatseekersimulatorjar.util.MathFunctions.Companion.rpmTommps
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage

class SpeedCalculatorWindow(val robot: Robot) : Application() {
    init {
        FW().readSpeedsFromCSV().let {
            robot.mps = it[0]
            robot.calculateSpeeds(fps)
        }
    }

    override fun start(primaryStage: Stage) {
        val rpmLabel = Label("Enter RPM:")
        val rpmInput = TextField()
        val diameterLabel = Label("Enter wheel diameter (mm):")
        val diameterInput = TextField()
        val gearRatioLabel = Label("Enter gear ratio:")
        val gearRatioInput = TextField()
        val speedLabel = Label("Enter speed (m/s):")
        val speedInput = TextField(robot.mps.toString())
        val calculateButton = Button("Calculate")
//        val resultLabel = Label()

        speedInput.setOnKeyReleased {
            val speed = speedInput.text.toDoubleOrNull()
            if (speed != null) {
//                resultLabel.text = "Speed: $speed m/s"
                robot.mps = speed
                robot.calculateSpeeds(fps)
                FW().writeSpeedsToCSV(
                    listOf(
                        speed,
                        diameterInput.text.toDoubleOrNull() ?: 0.0,
                        rpmInput.text.toDoubleOrNull() ?: 0.0,
                        gearRatioInput.text.toDoubleOrNull() ?: 0.0
                    )
                )
            }
        }
        calculateButton.setOnAction {
            val rpm = rpmInput.text.toDoubleOrNull()
            val diameter = diameterInput.text.toDoubleOrNull()
            val gearRatio = gearRatioInput.text.toDoubleOrNull()
            if (rpm != null && diameter != null && gearRatio != null) {
                val speed = mmpsTomps(rpmTommps(applyGearRatio(rpm, gearRatio), diameter))
//                resultLabel.text = "Speed: $speed m/s"
                robot.mps = speed
                robot.calculateSpeeds(fps)
                speedInput.text = speed.toString()
                FW().writeSpeedsToCSV(listOf(speed, diameter, rpm, gearRatio))
            } else {
                val speed = speedInput.text.toDoubleOrNull()
                if (speed != null) {
//                    resultLabel.text = "Speed: $speed m/s"
                    robot.mps = speed
                    robot.calculateSpeeds(fps)
                    speedInput.text = speed.toString()
                    FW().writeSpeedsToCSV(
                        listOf(
                            speed,
                            diameter ?: 0.0,
                            rpm ?: 0.0,
                            gearRatio ?: 0.0
                        )
                    )
                }
            }
        }
        speedLabel.style = "-fx-text-fill: #ffffff;" // Red color
        diameterLabel.style = "-fx-text-fill: #ffffff;" // Green color
        rpmLabel.style = "-fx-text-fill: #ffffff;" // Blue color
        gearRatioLabel.style = "-fx-text-fill: #ffffff;" // Yellow color


        val layout = VBox(
            10.0,
            speedLabel,
            speedInput,
            diameterLabel,
            diameterInput,
            rpmLabel,
            rpmInput,
            gearRatioLabel,
            gearRatioInput,
            calculateButton,
//            resultLabel
        )
        layout.style = "-fx-background-color: #2b2b2b; -fx-text-fill: #ffffff;"
        val scene = Scene(layout, 300.0, 600.0)

        val popupStage = Stage()
        popupStage.initModality(Modality.APPLICATION_MODAL)
        popupStage.isResizable = false
        popupStage.title = "Speed Calculator"
        popupStage.scene = scene
        popupStage.showAndWait()
    }
}