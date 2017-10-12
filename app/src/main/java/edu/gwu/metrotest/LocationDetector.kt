package edu.gwu.metrotest

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.location.LocationListener
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import java.util.*
import kotlin.concurrent.timerTask
import android.content.pm.PackageManager
import android.Manifest.permission
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.support.v4.app.ActivityCompat
import android.Manifest.permission.ACCESS_FINE_LOCATION




/**
 * Created by liteng on 10/7/17.
 */

//}

//class LocationDetector(val context: Context): LocationListener {
//    private var location: Location? = null
//    val TAG = "LocationDector"
//    var locationListener: LocationListener? = null
//    var locationManager: LocationManager? = null
//    var gpsProviderEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)
//    var networkProviderEnabled = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//
//    enum class FailureReason {
//        TIMEOUT,
//        NO_PERMISSION
//    }
//
//    interface LocationListener {
//        fun locationFound(location: Location)
//        fun locationNotFound(reason: FailureReason)
//    }
//
//    @SuppressLint("MissingPermission")
//    fun detectLocation(context: Context) {
//        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        val permissionResult = ContextCompat.checkSelfPermission(context,
//                android.Manifest.permission.ACCESS_FINE_LOCATION)
//
//        if(permissionResult == android.content.pm.PackageManager.PERMISSION_GRANTED) {
//
//            if (gpsProviderEnabled!! || networkProviderEnabled!!) {
//                location = locationManager?.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
//
//                if(!locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)!!) {
//                    locationListener?.locationNotFound(FailureReason.NO_PERMISSION)
//                } else {
//                    if (location == null) {
//                        Log.v("can't !!!!!!", "!!!!!!!!!")
//                    } else {
//                        locationListener?.locationFound(location!!)
//                    }
//
//                }
//
//                locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                        3, 3f, this@LocationDetector)
//            } else {
//                locationListener?.locationNotFound(FailureReason.NO_PERMISSION)
//            }
//        }
//
//    }
//
//    override fun onLocationChanged(location : Location?) {
//        location?.let {
//            locationListener?.locationFound(location)
//        }
//        stopLocationUpdates()
//    }
//
//    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
//        //TODO
//    }
//
//    override fun onProviderEnabled(p0: String?) {
//        //TODO
//    }
//
//    override fun onProviderDisabled(p0: String?) {
//        //TODO
//    }
//
//
//    fun stopLocationUpdates() {
//        locationManager?.removeUpdates(this)
//    }
//}

//class LocationDetector(val context: Context) {
//    private var location: Location? = null
//    val TAG = "LocationDector"
//    var locationListener: LocationListener? = null
//    var locationManager: LocationManager? = null
//
//    enum class FailureReason {
//        TIMEOUT,
//        NO_PERMISSION
//    }
//
//    interface LocationListener {
//        fun locationFound(location: Location)
//        fun locationNotFound(reason: FailureReason)
//    }
//
//
//
//    fun detectLocation() {
//        if (gpsProviderEnabled || networkProviderEnabled) {
//            if (networkProviderEnabled) {
//                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, this)
//                if (locationManager != null) {
//                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//                    providerType = "network"
//                    Log.d(LOG_TAG, "network lbs provider:" + if (location == null) "null" else String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()))
//                    updateLocation(location)
//                }
//            }
//
//            if (gpsProviderEnabled && location == null) {
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, this)
//                if (locationManager != null) {
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        return
//                    }
//                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//                    providerType = "gps"
//                    Log.d(LOG_TAG, "gps lbs provider:" + if (location == null) "null" else String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()))
//                    updateLocation(location)
//                }
//            }
//        }
//    }
//}





class LocationDetector(val context: Context) {
    val fusedLocationClient: FusedLocationProviderClient

    init {
        fusedLocationClient = FusedLocationProviderClient(context)
    }

    //enum to describe reasons location detection might fail
    enum class FailureReason {
        TIMEOUT,
        NO_PERMISSION
    }

    var locationListener: LocationListener? = null

    interface LocationListener {
        fun locationFound(location: Location)
        fun locationNotFound(reason: FailureReason)
    }

    fun detectLocation() {
        //create location request
        val locationRequest = LocationRequest()
        locationRequest.interval = 0L

        //check for location permission
        val permissionResult = ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        //if location permission granted, proceed with location detection
        if(permissionResult == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            //create timer
            val timer = Timer()

            //create location detection callback
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {


                    //cancel timer
                    timer.cancel()

                    //fire callback with location
                    locationListener?.locationFound(locationResult.locations.first())
                    //stop location updates
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }

            //start a timer to ensure location detection ends after 10 seconds
            timer.schedule(timerTask {
                //if timer expires, stop location updates and fire callback
                fusedLocationClient?.removeLocationUpdates(locationCallback)
                locationListener?.locationNotFound(FailureReason.TIMEOUT)
            }, 10*1000) //10 seconds


            //start location updates
            fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, null)
        }
        else {
            //else if no permission, fire callback
            locationListener?.locationNotFound(FailureReason.NO_PERMISSION)
        }
    }
}

