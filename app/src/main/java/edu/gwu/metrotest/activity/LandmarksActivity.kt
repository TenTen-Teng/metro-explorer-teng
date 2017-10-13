package edu.gwu.metrotest.activity


import android.content.Intent
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import edu.gwu.metrotest.LocationDetector
import edu.gwu.metrotest.PersistanceManager
import edu.gwu.metrotest.asyncTask.FetchLandmarksAsyncTask
import edu.gwu.metrotest.adapter.LandmarksAdapter
import edu.gwu.metrotest.R
import edu.gwu.metrotest.asyncTask.FetchMetroStationAsyncTask
import edu.gwu.metrotest.model.Landmark
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
    private var metroStation = ""

    //private var locationDetector = LocationDetector(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmarks)
        //locationDetector = LocationDetector(this@LandmarksActivity)
        //locationDetector.locationListener = this
        initView()
    }

    fun initView() {
        loadingBar(true)

        recyclerView = findViewById(R.id.landmarks_list)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false)
        //locationDetector.detectLocation(this)

        fetchMetroStationAsyncTask.findStationNameListener = this
        fetchMetroStationAsyncTask.findStationCode("38.858056", "-77.057778")

    }

    override fun onClick(p0: View?) {
        val intent = Intent(this, LandmarkDetailActivity::class.java)
        val holder = p0?.tag as LandmarksAdapter.ViewHolder
        intent.putExtra(LandmarkDetailActivity.LANDMARK, landmarksAdapter?.getLandmark(holder.adapterPosition))
        startActivity(intent)
    }

    override fun landmarkItemLoaded(landmarks: ArrayList<Landmark>) {
        landmarksAdapter?:let {
            landmarksAdapter = LandmarksAdapter(landmarks, this)
            recyclerView.adapter = landmarksAdapter
        }
        loadingBar(false)
    }

    override fun landmarkItemNotLoaded() {
        loadingBar(false)
        toast("Item didn't load :(")
    }

    override fun locationFound(location: Location) {
        Log.e("location@@@@", location.toString())
        toast("location ${location.latitude}, ${location.longitude}")

        fetchMetroStationAsyncTask.findStationNameListener = this
        fetchMetroStationAsyncTask.findStationCode(location.latitude.toString(), location.longitude.toString())

        finish()
    }

    override fun locationNotFound(reason: LocationDetector.FailureReason) {
        //showLoading(false)
        when(reason){
            LocationDetector.FailureReason.TIMEOUT -> Log.d(TAG, "Location timed out!!!")
            LocationDetector.FailureReason.NO_PERMISSION -> Log.d(TAG, "No location permission")
        }
        finish()
    }

    override fun stationNameFound(stationName : String) {
        fetchLandmarksAsyncTask.itemSearchCompletionListener = this
        landmarks = fetchLandmarksAsyncTask.loadLandmarkData(stationName)
        metroStation = stationName
    }

    override fun stationNameNotFound() {
        alert("Can't find closest Metro Station :( ") {
            yesButton {  }
        }
    }

    private fun loadingBar(show: Boolean) {
        if(show) {
            progress_bar.visibility = ProgressBar.VISIBLE
        }
        else {
            progress_bar.visibility = ProgressBar.INVISIBLE
        }
    }

}
