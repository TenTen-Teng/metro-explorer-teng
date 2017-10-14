package edu.gwu.metrotest.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.location.*
import java.util.*
import kotlin.concurrent.timerTask


/**
 * Created by liteng on 10/7/17.
 */

class LocationDetector(val context: Context): AppCompatActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    var locationListener: LocationListener? = null


    interface LocationListener {
        fun locationFound(location : Location)
        fun locationNotFound(reason : FailureReason)
    }

    enum class FailureReason {
        TIMEOUT,
        NO_PERMISSION
    }

    @SuppressLint("MissingPermission")
    fun detectLocation(context: Context) {
        val locationRequest = LocationRequest()

        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.interval = 5 * 1000

        val locationSettingBuilder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        locationSettingBuilder.addLocationRequest(locationRequest)
        val locationSetting: LocationSettingsRequest = locationSettingBuilder.build()

        val client: SettingsClient = LocationServices.getSettingsClient(context)
        client.checkLocationSettings(locationSetting)

        val permissionResult = ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)

        //if location permission granted, proceed with location detection
        if(permissionResult == android.content.pm.PackageManager.PERMISSION_GRANTED) {

            //create timer
            val timer = Timer()

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    timer.cancel()
                    Log.e("~~~~~~~~~~333333", "locationFound!!!!")
                    locationListener?.locationFound(locationResult.lastLocation)
                }

                override fun onLocationAvailability(p0: LocationAvailability?) {
                    super.onLocationAvailability(p0)
                }
            }

            fusedLocationProviderClient.removeLocationUpdates(locationCallback)

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

            timer.schedule(timerTask {
                //if timer expires, stop location updates and fire callback
                fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
                locationListener?.locationNotFound(FailureReason.TIMEOUT)
            }, 10*1000) //10 seconds

        } else {
            locationListener?.locationNotFound(FailureReason.NO_PERMISSION)
        }
    }


    fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}
