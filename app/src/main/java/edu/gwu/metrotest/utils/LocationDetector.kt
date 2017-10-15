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
 * Created by jared on 10/8/17.
 */

class LocationDetector(val context: Context): AppCompatActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    var locationListener: LocationListener? = null


    interface LocationListener {
        fun locationFound(location : Location)
        fun locationNotFound(reason : FailureReason)
    }

    //enum to describe reasons location detection might fail
    enum class FailureReason {
        TIMEOUT,
        NO_PERMISSION
    }

    @SuppressLint("MissingPermission")
    fun detectLocation(context: Context) {
        //create location request
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

            //create location detection callback
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    timer.cancel()
                    //fire callback with location
                    locationListener?.locationFound(locationResult.lastLocation)
                }

                override fun onLocationAvailability(p0: LocationAvailability?) {
                    super.onLocationAvailability(p0)
                }
            }

            //stop location updates
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)

            //start location updates
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

            //start a timer to ensure location detection ends after 10 seconds
            timer.schedule(timerTask {
                //if timer expires, stop location updates and fire callback
                fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
                locationListener?.locationNotFound(FailureReason.TIMEOUT)
            }, 10*1000) //10 seconds

        } else {
            //else if no permission, fire callback
            locationListener?.locationNotFound(FailureReason.NO_PERMISSION)
        }
    }


    fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}
