// SimulatorActivity.kt
package com.gentrifiedapps.heatseekersimulator

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

class SimulatorActivity : AppCompatActivity() {
    private val viewModel: SimulatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a LinearLayout
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
        }

        // Create a TextView for simulation status
        val simulationStatus = TextView(this).apply {
            text = "Simulation status will be shown here"
        }

        // Create Start button
        val startButton = Button(this).apply {
            text = "Start Simulation"
            setOnClickListener {
                viewModel.startSimulation()
            }
        }

        // Create Stop button
        val stopButton = Button(this).apply {
            text = "Stop Simulation"
            setOnClickListener {
                viewModel.stopSimulation()
            }
        }

        // Add views to the layout
        layout.addView(simulationStatus)
        layout.addView(startButton)
        layout.addView(stopButton)

        // Set the layout as the content view
        setContentView(layout)

        // Observe the simulation data
        viewModel.simulationData.observe(this, Observer { data ->
            simulationStatus.text = data
        })
    }
}