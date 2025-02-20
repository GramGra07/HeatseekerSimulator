package com.gentrifiedapps.heatseekersimulatorjar

import com.gentrifiedapps.heatseekersimulatorjar.GlobalVals.Companion.centerWaypoint
import com.gentrifiedapps.heatseekersimulatorjar.GlobalVals.Companion.height
import com.gentrifiedapps.heatseekersimulatorjar.GlobalVals.Companion.imageParam
import com.gentrifiedapps.heatseekersimulatorjar.GlobalVals.Companion.inToPixels
import com.gentrifiedapps.heatseekersimulatorjar.GlobalVals.Companion.toolbarHeight
import com.gentrifiedapps.heatseekersimulatorjar.GlobalVals.Companion.waypointRad
import com.gentrifiedapps.heatseekersimulatorjar.GlobalVals.Companion.width
import com.gentrifiedapps.heatseekersimulator.drawers.ToolbarDrawer
import com.gentrifiedapps.heatseekersimulatorjar.util.Formattables
import com.gentrifiedapps.heatseekersimulatorjar.util.MathFunctions
import com.gentrifiedapps.heatseekersimulatorjar.util.MathFunctions.Companion.distanceTo
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.SplitPane
import javafx.scene.control.TextArea
import javafx.scene.image.Image
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.stage.Stage
import java.io.FileInputStream
import com.gentrifiedapps.heatseekersimulatorjar.util.Waypoint

class RoboticsSimulator : Application() {
    var robot: Robot = Robot()
    var cursorPoint = Waypoint(0.0, 0.0, 0.0, 0.0)
    var pubwaypoints = mutableListOf<Waypoint>()

    var lastFrameTime: Long = 0
    var fps: Double = 0.0

    var totalTime = 0.0

    override fun start(primaryStage: Stage) {
        val splitPane = SplitPane()
        splitPane.style =
            "-fx-background-color: #2b2b2b; -fx-control-inner-background: #2b2b2b; -fx-text-fill: #ffffff;"

        // Code Editor (Simple WebView for now, can be replaced with a real editor component)
        val canvasC = Canvas(width / 2, height / 3 * 2 - 100)
        val codeEditor = TextArea()
        codeEditor.style = "-fx-control-inner-background: #2b2b2b; -fx-text-fill: #ffffff;"
        codeEditor.setOnKeyReleased {
            if (codeEditor.text.isNotEmpty()) {
                pubwaypoints = Formattables().formatString(codeEditor)
            }
        }

        val simPaneCode = VBox(
            HBox(
                robot.pidControllerViewX.draw("X controller", 0),
                robot.pidControllerViewY.draw("Y controller", 1),
                robot.pidControllerViewH.draw("H controller", 2)
            ), StackPane(canvasC, codeEditor)
        )
        robot.pidControllerViewH.setOnReleased(
            robot.pidControllerViewX,
            robot.pidControllerViewY,
            robot.pidControllerViewH
        )
        robot.pidControllerViewX.setOnReleased(
            robot.pidControllerViewX,
            robot.pidControllerViewY,
            robot.pidControllerViewH
        )
        robot.pidControllerViewY.setOnReleased(
            robot.pidControllerViewX,
            robot.pidControllerViewY,
            robot.pidControllerViewH
        )


        // Simulator Canvas
        val canvas = Canvas(width / 2 + 20 * inToPixels, height - 100)
        canvas.style = "-fx-control-inner-background: #2b2b2b; -fx-text-fill: #ffffff;"
        canvas.setOnMouseMoved { event ->
            val cursorX = event.x
            val cursorY = event.y
            cursorPoint = Waypoint(cursorX, cursorY, 0.0, 0.0)
        }
        canvas.setOnMouseClicked { event ->
            val cursorX = event.x.toInt()
            val cursorY = event.y.toInt()
            if (cursorX < imageParam && cursorY < imageParam) {
                val newPoint = Formattables().wrapPixToWay(
                    Waypoint(
                        cursorX.toDouble(),
                        cursorY.toDouble(),
                        90.0,
                        1.0
                    )
                )
                codeEditor.text += ".addWaypoint(new Waypoint(new Target2D(${newPoint.x.toInt()}, ${newPoint.y.toInt()}, new Angle(${newPoint.h}, AngleUnit.DEGREES)), ${newPoint.velocity}))\n"
            }
        }
        val gc = canvas.graphicsContext2D
        val timingCanvas = Canvas(width / 2 + 20 * inToPixels, 50.0)

        // Text Field
        val timingText = Text()
        timingText.fill = Color.WHITE
        timingText.font = javafx.scene.text.Font.font(20.0)

        val stackPane = VBox(canvas, StackPane(timingCanvas, timingText))
        stackPane.style = "-fx-background-color: #2b2b2b;"

        val simulatorPane = VBox(stackPane)
        simulatorPane.style = "-fx-background-color: #2b2b2b;"

        splitPane.items.addAll(simPaneCode, simulatorPane)
        splitPane.setDividerPositions(0.4)

        val simulator = Simulator(robot, gc)
        val toolbarDrawer = ToolbarDrawer(robot, simulator, primaryStage)

        val root = VBox(toolbarDrawer.toolbar, splitPane)
        val scene = Scene(root, width + 20 * inToPixels, height - 20)

        primaryStage.isResizable = false
        primaryStage.title = "Heatseeker Simulator"
        primaryStage.scene = scene
        primaryStage.show()
        primaryStage.setOnShown {
            toolbarHeight = toolbarDrawer.toolbar.layoutBounds.height
        }
        robot.setPos(centerWaypoint)

        object : AnimationTimer() {
            override fun handle(now: Long) {
                if (lastFrameTime > 0) {
                    val deltaTime = now - lastFrameTime
                    fps = 1_000_000_000.0 / deltaTime
                }
                lastFrameTime = now
                fps = GlobalVals.fps

                gc.clearRect(0.0, 0.0, canvas.width, canvas.height) // Clear the canvas
                drawBg(gc)
                drawCoords(gc)
                //refresh waypoints
                pubwaypoints = Formattables().formatString(codeEditor)
                drawWaypoints(gc, pubwaypoints)
                timingText.text = "Time (s): ${String.format("%.2f", totalTime).toDouble()}"
                simulator.update(pubwaypoints)
            }
        }.start()
    }

    private fun drawWaypoints(gc: GraphicsContext, waypoints: List<Waypoint>) {
        gc.globalAlpha = 0.9
        // lines in between
        gc.stroke = Color.BLACK
        gc.lineWidth = 2.0

        var waypointsWrapped = emptyList<Waypoint>()
        waypoints.forEach {
            waypointsWrapped += Formattables().wrapWaypointToPix(it)
        }
        pubwaypoints = waypointsWrapped.toMutableList()

        for (i in 0 until waypointsWrapped.size - 1) {
            gc.strokeLine(
                waypointsWrapped[i].x,
                waypointsWrapped[i].y,
                waypointsWrapped[i + 1].x,
                waypointsWrapped[i + 1].y
            )
        }
        gc.fill = Color.RED
        waypointsWrapped.forEach {
            gc.fillOval(it.x - waypointRad, it.y - waypointRad, waypointRad * 2, waypointRad * 2)
        }
        sumWaypoint(waypointsWrapped)
    }

    private fun sumWaypoint(waypoints: List<Waypoint>) {
        if (waypoints.isNotEmpty()) {
            //distance between all waypoints
            var sum = 0.0
            for (i in 0 until waypoints.size - 1) {
                sum += (waypoints[i].distanceTo(waypoints[i + 1]) * (1 / inToPixels)).toInt() * (1 / waypoints[i].velocity)
            }
            totalTime = (MathFunctions.inToMeters(sum) / robot.mps).toDouble()
        }
    }

    private fun drawBg(gc: GraphicsContext) {
        val loader =
            FileInputStream("C:\\Users\\grade\\Downloads\\repos\\HeatseekerSimulator\\app\\src\\main\\res\\image.png")
        val fieldImage = Image(loader)
        gc.drawImage(fieldImage, 0.0, 0.0, imageParam, imageParam)
    }

    private fun drawCoords(gc: GraphicsContext) {
        gc.stroke = Color.BLACK
        gc.fill = Color.WHITE
        val newPoint = Formattables().wrapPixToWay(cursorPoint)
        gc.fillText("Point (${newPoint.x.toInt()}, ${newPoint.y.toInt()})", 10.0, imageParam + 10)
    }
    //
}
fun main() {
    Application.launch(RoboticsSimulator::class.java)
}