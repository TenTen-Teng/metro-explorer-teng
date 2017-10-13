package edu.gwu.metrotest.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import edu.gwu.metrotest.PersistanceManager
import edu.gwu.metrotest.R
import edu.gwu.metrotest.model.Landmark
import kotlinx.android.synthetic.main.activity_landmark_detail.*
import org.jetbrains.anko.toast

class LandmarkDetailActivity : AppCompatActivity(){
    private lateinit var persistanceManager: PersistanceManager
    private var title: String = ""
    private var address1: String = ""
    private var address2: String = ""
    private var distance: Int = 0
    private var imageUrl: String = ""

    companion object {
        val LANDMARK = "LANDMARK"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmark_detail)

        setSupportActionBar(detail_toolbar)

        val landmark = intent.getParcelableExtra<Landmark>(LANDMARK)
        title = landmark.name
        address1 = landmark.address1
        address2 = landmark.address2
        distance = landmark.distance
        imageUrl = landmark.imageUrl

        persistanceManager = PersistanceManager(this)

        showDetail()
    }

    fun showDetail() {
        loadingBar(true)
        title_text.text = title
        address_text.text = address1 + "\n" + address2
        distance_text.text = distance.toString()
        //landmark_image


        loadingBar(false)
    }

    fun morePressed(item:MenuItem) {
        toast(R.string.like)
        val favLandmark = Landmark(title, imageUrl, address1, address2,
                distance)
        persistanceManager.saveFavorite(favLandmark)
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





