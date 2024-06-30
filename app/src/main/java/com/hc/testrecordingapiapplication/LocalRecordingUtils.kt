package com.hc.testrecordingapiapplication

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.fitness.FitnessLocal
import com.google.android.gms.fitness.LocalRecordingClient
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.data.LocalDataType
import com.google.android.gms.fitness.request.LocalDataReadRequest
import com.google.android.gms.fitness.result.LocalDataReadResponse
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.todayIn
import java.util.concurrent.TimeUnit

fun ComponentActivity.getLocalRecordingClient(localDataType: LocalDataType): Result<LocalRecordingClient> {
    return runCatching {
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) != ConnectionResult.SUCCESS) {
            throw Exception("GooglePlay not available")
        }

        val isAllOfPermissionsGranted = MainActivity.PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }

        if (!isAllOfPermissionsGranted) {
            requestPermissions(MainActivity.PERMISSIONS, 0)
            throw Exception("Permission not granted")
        }

        FitnessLocal.getLocalRecordingClient(this).apply {
            this.subscribe(localDataType)
                .addOnFailureListener { e ->
                    throw Exception("Subscription fail")
                }
        }
    }
}

fun LocalRecordingClient.readStepsOfToday(onSuccess: (Int) -> Unit) {
    val now = Clock.System.now()

    val timeZone = TimeZone.of("Asia/Seoul")
    val startOfToday = Clock.System.todayIn(timeZone).atStartOfDayIn(timeZone)

    readSteps(startOfToday, now, onSuccess)
}

private fun LocalRecordingClient.readSteps(startTime: Instant, endTime: Instant, onSuccess: (Int) -> Unit) {
    val localDataReadRequest = getLocalDataReadRequest(startTime, endTime)

    readData(localDataReadRequest)
        .addOnSuccessListener { response ->
            val steps = getSteps(response)
            onSuccess(steps)
        }
        .addOnFailureListener { e ->
            log("Read fail")
        }
}

private fun getLocalDataReadRequest(startTime: Instant, endTime: Instant): LocalDataReadRequest {
    return LocalDataReadRequest.Builder()
        .aggregate(LocalDataType.TYPE_STEP_COUNT_DELTA)
        .bucketByTime(1, TimeUnit.DAYS)
        .setTimeRange(startTime.epochSeconds, endTime.epochSeconds, TimeUnit.SECONDS)
        .build()
}

private fun getSteps(response: LocalDataReadResponse): Int {
    return response.buckets
        .flatMap { bucket ->
            bucket.dataSets
        }.flatMap { dataSet ->
            dataSet.dataPoints
        }.sumOf { dataPoint ->
            val stepsField = dataPoint.dataType.fields
                .firstOrNull {
                    it.name == Field.FIELD_STEPS.name
                }
            stepsField?.let { dataPoint.getValue(it).asInt() } ?: 0
        }
}