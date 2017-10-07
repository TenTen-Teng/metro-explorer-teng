package edu.gwu.metrotest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_menu)

        //click here
        closestStation_button.setOnClickListener {
            val intent2 = Intent(this, LandmarksActivity::class.java)
            startActivity(intent2)
        }

        selectStation_button.setOnClickListener {
            //TODO
        }

        favorite_landmarks_button.setOnClickListener {
            val intent3 = Intent(this, LandmarkDetailActivity::class.java)
            startActivity(intent3)
        }
    }

}

