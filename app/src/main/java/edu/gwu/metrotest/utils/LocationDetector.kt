package edu.gwu.metrotest.utils

import android.content.Context
import android.location.Location
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import java.util.*
import kotlin.concurrent.timerTask


/**
 * Created by liteng on 10/7/17.
 */

//class LocationDetector(val context: Context):AppCompatActivity(), LocationListener {
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
//    @SuppressLint("MissingPermission")
//    fun detectLocation(context: Context) {
//        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
//        location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//        var isGPS = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)!!
//
//        if(!isGPS) {
//            locationListener?.locationNotFound(FailureReason.NO_PERMISSION)
//
//        } else {
//            if (location == null) {
//                Log.v("can't !!!!!!", "!!!!!!!!!")
//            } else {
//                locationListener?.locationFound(location!!)
//            }
//        }
//        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this)
//
//    }
//
//    override fun onLocationChanged(location : Location?) {
//        location?.let {
//            Log.e("fromdetector", location.longitude.toString())
//            locationListener?.locationFound(location)
//        }
//        stopLocationUpdates()
//    }
//
//    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
//        //not used
//    }
//
//    override fun onProviderEnabled(p0: String?) {
//        //not used
//    }
//
//    override fun onProviderDisabled(p0: String?) {
//        //not used
//    }
//
//
//    fun stopLocationUpdates() {
//        locationManager?.removeUpdates(this)
//    }
//}
//
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
//            if (gpsProviderEnabled != null || networkProviderEnabled != null) {
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

    fun detectLocation(context: Context) {
        //create location request
        val locationRequest = LocationRequest()
        locationRequest.interval = 0L

        //check for location permission
        val permissionResult = ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)

        //if location permission granted, proceed with location detection
        if(permissionResult == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            //create timer
            val timer = Timer()

            //create location detection callback
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    fusedLocationClient.removeLocationUpdates(this)
                    //cancel timer
                    timer.cancel()

                    //fire callback with location
                    locationListener?.locationFound(locationResult.locations.first())
                    //stop location updates

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

