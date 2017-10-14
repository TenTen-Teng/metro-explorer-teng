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
import android.content.Intent
import android.net.Uri

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

        setSupportActionBar(like_toolbar)
        setSupportActionBar(share_toolbar)

        val landmark = intent.getParcelableExtra<Landmark>(LANDMARK)
        title = landmark.name
        address1 = landmark.address1
        address2 = landmark.address2
        distance = landmark.distance
        imageUrl = landmark.imageUrl

        persistanceManager = PersistanceManager(this)

        showDetail()

        map_button.setOnClickListener{
            // Create a Uri from an intent string. Use the result to create an Intent.
            val gmmIntentUri = Uri.parse("google.navigation:q=" + title)

            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            // Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps")

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                // Attempt to start an activity that can handle the Intent
                startActivity(mapIntent)
            } else {
                //doesn't have a map app
                toast("Sorry, can't find a map app :(")
            }
        }
    }

    fun showDetail() {
        loadingBar(true)
        title_text.text = title
        address_text.text = address1 + "\n" + address2
        distance_text.text = distance.toString()
        //landmark_image

        loadingBar(false)
    }

    fun likePressed(item:MenuItem) {
        toast(R.string.like)
        val favLandmark = Landmark(title, imageUrl, address1, address2,
                distance)
        persistanceManager.saveFavorite(favLandmark)
    }

    fun sharePressed(item:MenuItem) {
        toast(R.string.share)
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Share this to social media")
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)))
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.like, menu)
        menuInflater.inflate(R.menu.share, menu)

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





