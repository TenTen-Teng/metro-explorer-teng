package edu.gwu.metrotest.model

import android.os.Parcelable

/**
 * Created by liteng on 10/1/17.
 */


data class Landmark(val name: String, val imageUrl: String, val address1: String, val address2: String, val distance: Int)
