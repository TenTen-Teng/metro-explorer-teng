package edu.gwu.metrotest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import edu.gwu.metrotest.R.id.landmarks_list
import edu.gwu.metrotest.model.Landmark
import kotlinx.android.synthetic.main.activity_landmarks.*
import kotlinx.android.synthetic.main.list_landmark.*
import org.jetbrains.anko.activityUiThread
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast

class LandmarksActivity : AppCompatActivity(), View.OnClickListener, FetchLandmarksAsyncTask.ItemSearchCompletionListener{
    private val TAG = "LandmarksActivity"
    private lateinit var fetchLandmarksAsyncTask : FetchLandmarksAsyncTask
    var landmarksAdapter:LandmarksAdapter ?= null
    //var landmarkInfos = ArrayList<Landmark>()

    override fun onClick(p0: View?) {
        loadLandmarkDetailData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmarks)

        fetchLandmarksAsyncTask = FetchLandmarksAsyncTask(this)
        fetchLandmarksAsyncTask.itemSearchCompletionListener = this
        fetchLandmarksAsyncTask.loadLandmarkData()
        landmarks_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        landmarks_list.setOnClickListener(){

        }
    }

    fun loadLandmarkDetailData() {
        doAsync {
            //返回arrayList
            val landmarkDetailData = fetchLandmarksAsyncTask.loadLandmarkData()

            //if(landmarkData != null && landmarkData.size > 0) {
                activityUiThread {
                    val intent = Intent(this@LandmarksActivity, LandmarkDetailActivity::class.java)

                    //intent.putExtra("landmarkDetailData", landmarkDetailData)
                    intent.putExtra("ACTIVITY", "LandmarksDetailActivity")
                    intent.putExtra("name", landmarkDetailData[0].toString())
                    intent.putExtra("imageUrl", landmarkDetailData[1].toString())
                    intent.putExtra("distance", landmarkDetailData[4].toString())
                    //intent.putExtra("landmarkData", landmarkDetailData)
                    startActivity(intent)
                }
        }
    }

    override fun itemLoaded(landmarks: ArrayList<Landmark>) {
        //need add a prograss bar
        toast("Item is loading ....")

        landmarksAdapter?:let {
            landmarksAdapter = LandmarksAdapter(landmarks)
            landmarks_list.adapter = landmarksAdapter
        }

        landmarksAdapter?.setOnItemClickListener(this)
    }

    override fun itemNotLoaded() {
        toast("Item didn't load :(")
    }



}
