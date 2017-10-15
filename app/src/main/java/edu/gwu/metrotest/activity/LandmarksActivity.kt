package edu.gwu.metrotest.activity

import android.content.Intent
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import edu.gwu.metrotest.utils.LocationDetector
import edu.gwu.metrotest.asyncTask.FetchLandmarksAsyncTask
import edu.gwu.metrotest.adapter.LandmarksAdapter
import edu.gwu.metrotest.R
import edu.gwu.metrotest.activity.MetroStationActivity.Companion.METROSTATION
import edu.gwu.metrotest.asyncTask.FetchMetroStationAsyncTask
import edu.gwu.metrotest.model.Landmark
import edu.gwu.metrotest.model.MetroStation
import kotlinx.android.synthetic.main.activity_landmarks.*
import org.jetbrains.anko.*
import java.util.*

class LandmarksActivity : AppCompatActivity(), View.OnClickListener,
        FetchLandmarksAsyncTask.ItemSearchCompletionListener,
        FetchMetroStationAsyncTask.FindStationNameListener,
        LocationDetector.LocationListener {

    private val TAG = "LandmarksActivity"
    private lateinit var landmarks: ArrayList<Landmark>
    var landmarksAdapter:LandmarksAdapter ?= null
    private lateinit var recyclerView: RecyclerView
    private var fetchLandmarksAsyncTask = FetchLandmarksAsyncTask(this)
    private var fetchMetroStationAsyncTask = FetchMetroStationAsyncTask(this)
    private lateinit var locationDetector : LocationDetector


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmarks)

        recyclerView = findViewById(R.id.landmarks_list)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val intent: Intent = getIntent()
        val bundle = intent.extras
        val activity = bundle.getString("activity")

        //from metroStation activity
        if(activity.equals("MetroStation")) {
            loadingBar(true)
            val station = intent.getParcelableExtra<MetroStation>(METROSTATION)

            //get landmark information from yelp API
            fetchLandmarksAsyncTask.itemSearchCompletionListener = this
            landmarks = fetchLandmarksAsyncTask.loadLandmarkData(station.name)
        }

        //from menu activity
        if(activity.equals("Menu")) {
            loadingBar(true)

            //get current location
            locationDetector = LocationDetector(this@LandmarksActivity)
            locationDetector.locationListener = this
            locationDetector.detectLocation(this)
        }
    }

    override fun onPause() {
        super.onPause()

        val intent: Intent = getIntent()
        val bundle = intent.extras
        val activity = bundle.getString("activity")

        //stop update location when current activity pause
        if(activity.equals("Menu")) {
            locationDetector.stopLocationUpdates()
        }

    }

    //handle click item : get current item's information and go to landmark detail activity
    override fun onClick(p0: View?) {
        val intent = Intent(this, LandmarkDetailActivity::class.java)
        val holder = p0?.tag as LandmarksAdapter.ViewHolder
        intent.putExtra(LandmarkDetailActivity.LANDMARK, landmarksAdapter?.getLandmark(holder.adapterPosition))
        startActivity(intent)
    }

    //listener from fetchLandmarksAsyncTask: get a list of landmarks information and connect to adapter
    override fun landmarkItemLoaded(landmarks: ArrayList<Landmark>) {
        loadingBar(false)
        landmarksAdapter?:let {
            landmarksAdapter = LandmarksAdapter(landmarks, this)
            recyclerView.adapter = landmarksAdapter
        }
        loadingBar(false)
    }

    //listener from fetchLandmarksAsyncTask: can't load items
    override fun landmarkItemNotLoaded() {
        loadingBar(false)
        toast("Item didn't load :(")
    }

    //listener from LocationDetector: get location
    override fun locationFound(location: Location) {
        loadingBar(false)

        //send location to fetchMetroStation to get closest metro station by searching WMATA API
        fetchMetroStationAsyncTask.findStationNameListener = this
        fetchMetroStationAsyncTask.findStationCode(location.latitude.toString(), location.longitude.toString())
    }

    //listener from LocationDetector: can't get current location
    override fun locationNotFound(reason: LocationDetector.FailureReason) {
        loadingBar(false)

        //toast a reason
        when(reason){
            LocationDetector.FailureReason.TIMEOUT -> toast("Location timed out :( ")
            LocationDetector.FailureReason.NO_PERMISSION -> toast("No location permission :( ")
        }

        finish()
    }

    //listener from fetchMetroStation: get closest metro station name
    override fun stationNameFound(stationName : String) {

        //pass station name as location attribute to find landmarks in Yelp API
        fetchLandmarksAsyncTask.itemSearchCompletionListener = this
        landmarks = fetchLandmarksAsyncTask.loadLandmarkData(stationName)
    }

    //listener from fetchMetroStation: can't find station name
    override fun stationNameNotFound() {
        loadingBar(false)
        //toast a info
        toast("Can't find closest Metro Station :( ")
    }

    //Progress bar
    private fun loadingBar(show: Boolean) {
        if(show) {
            progress_bar.visibility = ProgressBar.VISIBLE
        }
        else {
            progress_bar.visibility = ProgressBar.INVISIBLE
        }
    }

}
