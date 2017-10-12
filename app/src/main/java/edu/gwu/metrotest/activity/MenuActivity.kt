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
import org.jetbrains.anko.toast
import android.content.DialogInterface
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton


class MenuActivity : AppCompatActivity(){
    private val TAG = "MenuActivity"
    private val LOCATION_PERMISSION = 66


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        closestStation_button.setOnClickListener {
            val intent = Intent(this@MenuActivity, LandmarksActivity::class.java)
            startActivity(intent)
        }
//
        selectStation_button.setOnClickListener {
            val intent = Intent(this@MenuActivity, MetroStationActivity::class.java)
            startActivity(intent)
        }

//        favorite_landmarks_button.setOnClickListener {
//            //val intent3 = Intent(this, LandmarkDetailActivity::class.java)
//            //startActivity(intent3)
//        }

        requestPermissionsIfNecessary()
    }

    fun requestPermissionsIfNecessary() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检测获取地址的权限， 获取到返回 PackageManager.PERMISSION_GRANTED， 否则返回PERMISSION_DENIED
            val checkSelfPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) // 精确

            //如果没有获取到权限
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

                // Should we show an explanation?
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
                toast("got permission!")
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


