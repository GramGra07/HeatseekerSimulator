package com.gentrifiedapps.heatseekersimulator

import com.gentrifiedapps.heatseekersimulator.GlobalVals.Companion.height
import com.gentrifiedapps.heatseekersimulator.GlobalVals.Companion.imageParam
import com.gentrifiedapps.heatseekersimulator.GlobalVals.Companion.inToPixels
import com.gentrifiedapps.heatseekersimulator.GlobalVals.Companion.width
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import org.gentrifiedApps.gentrifiedAppsUtil.heatseeker.generics.PIDController
import kotlin.math.sqrt

class PIDControllerView {
    val textLabelP = Text("P:")
    val textLabelI = Text("I:")
    val textLabelD = Text("D:")
    val p = TextField("0.0")
    val i = TextField("0.0")
    val d = TextField("0.0")
    val spacer = HBox().apply { minWidth = 10.0 }

    fun getPID(): PIDController {
        return PIDController(p.text.toDoubleOrNull()?:0.0, i.text.toDoubleOrNull()?:0.0, d.text.toDoubleOrNull()?:0.0)
    }
    fun getMax():Double{
        val x = this.getPID().calculatePID(imageParam/(4* inToPixels),0.0)+0.000001
        return x
    }
    fun getList():List<Double>{
        return listOf(p.text.toDouble(),i.text.toDouble(),d.text.toDouble())
    }
    fun draw(label:String, index:Int):HBox{
        FW().readPIDValuesFromCSV()[index].let {
            p.text = it[0].toString()
            i.text = it[1].toString()
            d.text = it[2].toString()
        }
        val canvas = Canvas(width/2/3,height/3)
        val vbox = VBox(10.0,Text(label),textLabelP,p,textLabelI,i,textLabelD,d)
        vbox.alignment = javafx.geometry.Pos.CENTER
        val stackPane = HBox(spacer,StackPane(canvas,vbox))
        return stackPane
    }
    fun setOnReleased(xPidView: PIDControllerView, yPidView: PIDControllerView, hPidView: PIDControllerView){
        p.setOnKeyReleased {
            FW().writePIDValuesToCSV(listOf(xPidView.getList(),yPidView.getList(),hPidView.getList()))
        }
        i.setOnKeyReleased {
            FW().writePIDValuesToCSV(listOf(xPidView.getList(),yPidView.getList(),hPidView.getList()))
        }
        d.setOnKeyReleased {
            FW().writePIDValuesToCSV(listOf(xPidView.getList(),yPidView.getList(),hPidView.getList()))
        }
    }

}