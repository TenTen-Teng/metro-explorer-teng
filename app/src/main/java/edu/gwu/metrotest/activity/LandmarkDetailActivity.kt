package edu.gwu.metrotest.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.TextView
import edu.gwu.metrotest.PersistanceManager
import edu.gwu.metrotest.R
import edu.gwu.metrotest.model.Landmark
import kotlinx.android.synthetic.main.activity_landmark_detail.*
import org.jetbrains.anko.toast

class LandmarkDetailActivity : AppCompatActivity(){
    private lateinit var persistanceManager: PersistanceManager
    var favLandmark : Landmark ?= null

    companion object {
        val LANDMARK = "LANDMARK"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmark_detail)

        setSupportActionBar(detail_toolbar)

        loadingBar(true)

        val title = findViewById<TextView>(R.id.title_text)
        val address = findViewById<TextView>(R.id.address_text)
        val distance = findViewById<TextView>(R.id.distance_text)

        val landmark = intent.getParcelableExtra<Landmark>(LANDMARK)

        title.text = landmark.name

        if (landmark.address2 != "-1") {
            address.text = landmark.address1 + "\n" + landmark.address2
        } else {
            address.text = landmark.address1
        }

        distance.text = landmark.distance.toString()

        title.setOnClickListener {
            finish()
        }

        loadingBar(false)
    }

    fun morePressed(item:MenuItem) {
        toast(R.string.like)
        persistanceManager.saveFavorite(favLandmark!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.more, menu)

        return true
    }
    private fun loadingBar(show: Boolean) {
        if(show) {
            detail_progress_bar.visibility = ProgressBar.VISIBLE
        }
        else {
            detail_progress_bar.visibility = ProgressBar.INVISIBLE
        }
    }
}





