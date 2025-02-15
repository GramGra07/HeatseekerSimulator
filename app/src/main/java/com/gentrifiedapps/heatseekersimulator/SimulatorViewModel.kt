package com.gentrifiedapps.heatseekersimulator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SimulatorViewModel : ViewModel() {
    private val _simulationData = MutableLiveData<String>()
    val simulationData: LiveData<String> get() = _simulationData

    fun startSimulation() {
        // Add your simulation logic here
        _simulationData.value = "Simulation started"
    }

    fun stopSimulation() {
        // Add your simulation logic here
        _simulationData.value = "Simulation stopped"
    }
}