package edu.gwu.metrotest.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import edu.gwu.metrotest.asyncTask.FetchLandmarksAsyncTask
import edu.gwu.metrotest.adapter.LandmarksAdapter
import edu.gwu.metrotest.R
import edu.gwu.metrotest.model.Landmark
import edu.gwu.metrotest.model.LandmarkList
import kotlinx.android.synthetic.main.activity_landmarks.*
import org.jetbrains.anko.toast


class LandmarksActivity : AppCompatActivity(), View.OnClickListener, FetchLandmarksAsyncTask.ItemSearchCompletionListener {
    private val TAG = "LandmarksActivity"

    lateinit var landmarks: List<Landmark>
    var landmarksAdapter:LandmarksAdapter ?= null
    lateinit var recyclerView: RecyclerView
    var fetchLandmarksAsyncTask = FetchLandmarksAsyncTask(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmarks)

        initView()
    }

    fun initView() {
        recyclerView = findViewById(R.id.landmarks_list)
        landmarks_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        fetchLandmarksAsyncTask.itemSearchCompletionListener = this
        fetchLandmarksAsyncTask.loadLandmarkData()

        //landmarks_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun onClick(p0: View?) {
        val intent = Intent(this, LandmarkDetailActivity::class.java)
        val holder = p0?.tag as LandmarksAdapter.ViewHolder
        intent.putExtra(LandmarkDetailActivity.LANDMARK, landmarksAdapter?.getLandmark(holder.adapterPosition))
        startActivity(intent)
    }

    override fun landmarkItemLoaded(landmarks: ArrayList<Landmark>) {
        //need add a prograss bar
        toast("Item is loading ....")

        landmarksAdapter?:let {
            this@LandmarksActivity.landmarks = fetchLandmarksAsyncTask.loadLandmarkData()
            landmarksAdapter = LandmarksAdapter(landmarks, this)
            landmarks_list.adapter = landmarksAdapter
        }
        //landmarksAdapter?.setOnItemClickListener(this)
    }

    override fun landmarkItemNotLoaded() {
        toast("Item didn't load :(")
    }
}
