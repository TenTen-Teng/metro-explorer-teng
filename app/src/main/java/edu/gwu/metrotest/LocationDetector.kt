package edu.gwu.metrotest

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import org.jetbrains.anko.toast
import java.security.AccessControlContext

/**
 * Created by liteng on 10/7/17.
 */

class LocationDetector(val context: Context): AppCompatActivity() {
    private lateinit var locationManager: LocationManager
    //var locationDetectorCompletionListener: LocationDetectorCompletionListener? = null
    interface LocationDetectorCompletionListener {
        fun locationLoaded()
        fun locationNotLoaded()

    }

    //定义一个权限COde，用来识别Location权限
    private val LOCATION_PERMISSION = 1

    //使用匿名内部类创建了LocationListener的实例
    val locationListener = object : LocationListener {
        override fun onProviderDisabled(provider: String?) {
            Log.e("=====================", "locationListener-onProviderDisabled")
            toast("GPS is turn off")
        }

        override fun onProviderEnabled(provider: String?) {
            Log.e("=====================", "locationListener-onProviderEnabled")
            toast("turn on GPS")
            showLocation(locationManager)
        }

        override fun onLocationChanged(location: Location?) {
            Log.e("=====================", "locationListener-onLocationChanged")
            toast("location changed")
            showLocation(locationManager)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            Log.e("=====================", "locationListener-onStatusChanged")
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    //申请下位置权限后，要刷新位置信息
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.e("=====================", "onRequestPermissionsResult")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                toast("got permission")
                showLocation(locationManager)
            }
        }
    }

    fun showLocation(locationManager: LocationManager) : String {
        Log.e("=====================", "showLocation")
        var location = getLocation(locationManager).toString()

        return location
    }//获取位置信息

    @SuppressLint("MissingPermission")
    fun getLocation(locationManager: LocationManager): Location? {
        Log.e("=====================", "getLocation")

        var location: Location? = null

        if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            toast("no permission access GPS")
        } else if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            toast("Please turn on GPS")
            //引导用户打开GPS
        } else {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location == null) {
                toast("location is null")
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (location == null) {
                    toast("network information is null")
                }
                else {
                    toast("using current location")
                }
            }
        }
        Log.e("location========", location.toString())
        return location
    }
}