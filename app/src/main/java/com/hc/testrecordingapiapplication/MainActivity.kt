package com.hc.testrecordingapiapplication

import android.Manifest
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.google.android.gms.fitness.LocalRecordingClient
import com.google.android.gms.fitness.data.LocalDataType
import com.hc.testrecordingapiapplication.databinding.ActivityMainBinding

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var binding: ActivityMainBinding

    private var stepsRecordingClient: LocalRecordingClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        stepsRecordingClient = getLocalRecordingClient(LocalDataType.TYPE_STEP_COUNT_DELTA).getOrNull()

        registerStepDetectorSensor()

        stepsRecordingClient?.readStepsOfToday { steps ->
            binding.stepsByDetector = steps
            binding.stepsByRecordingApi = steps
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterStepDetectorSensor()
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        if (sensorEvent.sensor.type != Sensor.TYPE_STEP_DETECTOR) return

        increaseStepsByDetector()
        updateStepsByRecordingApi()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    private fun increaseStepsByDetector() {
        binding.stepsByDetector = (binding.stepsByDetector ?: 0) + 1
    }

    private fun updateStepsByRecordingApi() {
        stepsRecordingClient?.readStepsOfToday { steps ->
            binding.stepsByRecordingApi = steps
        }
    }

    companion object {
        val PERMISSIONS = arrayOf(
            Manifest.permission.ACTIVITY_RECOGNITION,
        )
    }
}

fun log(message: String) {
    Log.i("recording_api", message)
}