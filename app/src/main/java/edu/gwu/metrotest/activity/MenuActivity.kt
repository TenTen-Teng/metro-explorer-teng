package edu.gwu.metrotest.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import edu.gwu.metrotest.R
import kotlinx.android.synthetic.main.activity_menu.*
import org.jetbrains.anko.*


class MenuActivity : AppCompatActivity(){
    private val LOCATION_PERMISSION = 66

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        //click closest station button, go to landmark activity
        closestStation_button.setOnClickListener {
            val intent = Intent(this@MenuActivity, LandmarksActivity::class.java)
            intent.putExtra("activity", "Menu")
            startActivity(intent)
        }

        //click select station button, go to metro station activity
        selectStation_button.setOnClickListener {
            val intent = Intent(this@MenuActivity, MetroStationActivity::class.java)
            startActivity(intent)
        }

        //click favorite landmarks button, go to favorite landmark activity
        favorite_landmarks_button.setOnClickListener {
            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
        }

        //request location permission
        requestPermissionsIfNecessary()
    }


    fun requestPermissionsIfNecessary() {
        //check sdk version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //check permission and return PackageManager.PERMISSION_GRANTED if get, otherwise return PERMISSION_DENIED
            val checkSelfPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) // 精确

            //no permission
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                alert ("No permission granted"){
                    yesButton {
                        toast("Yesssss :) !!!")
                    }
                    noButton {
                        toast("Noooo :(")
                    }
                }
                ActivityCompat.requestPermissions(this@MenuActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION);

                // show an explanation
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    alert ("You need to allow access to Location to get the closest Metro Station"){
                        yesButton {
                            toast("Yesssss :) !!!")
                        }
                        noButton {
                            toast("Noooo :(")
                        }
                    }.show()
                } else {
                    //ask permission again
                    ActivityCompat.requestPermissions(this@MenuActivity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION);
                }
            } else {
                Log.v("permission","got permission")
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults.first() != PackageManager.PERMISSION_GRANTED) {
                requestPermissionsIfNecessary()
            } else {
                alert("Sorry, can't get location permission") {
                    yesButton {
                        toast("okay.... :| ")
                    }
                    noButton {
                    }
                }
            }
        }
    }
}


