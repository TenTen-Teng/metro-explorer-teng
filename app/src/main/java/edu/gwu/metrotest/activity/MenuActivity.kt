package edu.gwu.metrotest.activity

import android.content.Context
import android.content.Intent
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import edu.gwu.metrotest.LocationDetector
import edu.gwu.metrotest.R
import kotlinx.android.synthetic.main.activity_menu.*
import org.jetbrains.anko.activityUiThread
import org.jetbrains.anko.doAsync

class MenuActivity : AppCompatActivity(), LocationDetector.LocationDetectorCompletionListener {
    private val TAG = "MenuActivity"
    var locationDetector: LocationDetector ?= null
    private lateinit var locationManager: LocationManager
    var locationListener: LocationListener ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        closestStation_button.setOnClickListener {
            locationLoaded()
        }

        selectStation_button.setOnClickListener {
            val intent = Intent(this@MenuActivity, MetroStationActivity::class.java)
            startActivity(intent)
        }

        favorite_landmarks_button.setOnClickListener {
            val intent3 = Intent(this, LandmarkDetailActivity::class.java)
            startActivity(intent3)
        }
    }

    override fun locationLoaded() {
        doAsync {
            //返回arrayList，里面是两个坐标
            val locationData = locationDetector?.getLocation(locationManager)

            //if (locationData != null) {
                activityUiThread {
                    val intent = Intent(this@MenuActivity, LandmarksActivity::class.java)

                    intent.putExtra("locationData", locationData)
                    startActivity(intent)
                }

             //   Log.d(TAG, "location is ${locationData.toString()}}")
            //} else {
            //    Log.d(TAG, "problem")
            //}

        }
        //val intent2 = Intent(this, LandmarksActivity::class.java)
        //startActivity(intent2)
    }



    override fun locationNotLoaded() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}


