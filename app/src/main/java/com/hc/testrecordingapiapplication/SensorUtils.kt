package com.hc.testrecordingapiapplication

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager

fun MainActivity.registerStepDetectorSensor() {
    val sensorManager = this.getSensorManager() ?: return

    sensorManager.registerListener(
        this,
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
        SensorManager.SENSOR_DELAY_FASTEST
    )
}

fun Context.getSensorManager(): SensorManager? {
    return this@getSensorManager.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
}

fun MainActivity.unregisterStepDetectorSensor(): Result<Unit> {
    return runCatching {
        val sensorManager = getSensorManager() ?: throw Exception("Sensor not initialized")

        sensorManager.unregisterListener(this)
    }
}