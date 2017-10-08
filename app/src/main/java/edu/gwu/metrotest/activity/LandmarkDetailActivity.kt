package edu.gwu.metrotest.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import edu.gwu.metrotest.R
import edu.gwu.metrotest.model.Landmark

class LandmarkDetailActivity : AppCompatActivity(){
    companion object {
        val LANDMARK = "LANDMARK"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmark_detail)

        val title = findViewById<TextView>(R.id.title_text)
        val address = findViewById<TextView>(R.id.address_text)
        val distance = findViewById<TextView>(R.id.distance_text)

        val landmark = intent.getParcelableExtra<Landmark>(LANDMARK)

        title.text = landmark.name

        if (landmark.address2 != null) {
            address.text = landmark.address1 + "\n" + landmark.address2
        } else {
            address.text = landmark.address1
        }

        distance.text = landmark.distance.toString()

        title.setOnClickListener {
            finish()
        }
    }
}





