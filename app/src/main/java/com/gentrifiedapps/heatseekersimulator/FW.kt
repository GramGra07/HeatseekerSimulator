package com.gentrifiedapps.heatseekersimulator

import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import java.io.FileReader
import java.io.FileWriter

class FW {
    val pidName = "pid_vals.csv"
    fun readPIDValuesFromCSV(): List<List<Double>> {
        val reader = try {
            CSVReader(FileReader(pidName))
        }catch (e: Exception){
            if (e is java.io.FileNotFoundException) {
                writePIDValuesToCSV(listOf(listOf(0.0, 0.0, 0.0), listOf(0.0, 0.0, 0.0), listOf(0.0, 0.0, 0.0)))
                CSVReader(FileReader(pidName))
            }else{
                throw e
            }
        }
        val pidValues = mutableListOf<List<Double>>()
        var line: Array<String>?

        while (reader.readNext().also { line = it } != null) {
            val values = line!!.map { it.toDouble() }
            pidValues.add(values)
        }
        reader.close()
        return pidValues
    }

    fun writePIDValuesToCSV(pidValues: List<List<Double>>) {
        val writer = CSVWriter(FileWriter(pidName))
        pidValues.forEach { values ->
            val line = values.map { it.toString() }.toTypedArray()
            writer.writeNext(line)
        }
        writer.close()
    }

    val heightWidthName = "height_width.csv"
    fun readHeightWidthFromCSV(): List<Double> {
        val reader = try{CSVReader(FileReader(heightWidthName))}
        catch (e: Exception){
            if (e is java.io.FileNotFoundException) {
                writeHeightWidthToCSV(listOf(18, 18))
                CSVReader(FileReader(heightWidthName))
            }else{
                throw e
            }
        }
        val values = reader.readNext()!!.map { it.toDouble() }
        reader.close()
        return values
    }

    fun writeHeightWidthToCSV(values: List<Int>) {
        val writer = CSVWriter(FileWriter(heightWidthName))
        val line = values.map { it.toString() }.toTypedArray()
        writer.writeNext(line)
        writer.close()
    }

    val speedsName = "speeds.csv"

    //speed, diameter, rpm, gear ratio
    fun readSpeedsFromCSV(): List<Double> {
        val reader = try {
            CSVReader(FileReader(speedsName))
        } catch (e: Exception) {
            if (e is java.io.FileNotFoundException) {
                writeSpeedsToCSV(listOf(1.2))
                CSVReader(FileReader(speedsName))
            }else{
                throw e
            }
        }
        val values = reader.readNext()!!.map { it.toDouble() }
        reader.close()
        return values
    }

    fun writeSpeedsToCSV(values: List<Double>) {
        val writer = CSVWriter(FileWriter(speedsName))
        val line = values.map { it.toString() }.toTypedArray()
        writer.writeNext(line)
        writer.close()
    }
}