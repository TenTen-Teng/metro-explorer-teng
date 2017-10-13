package edu.gwu.metrotest

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.gwu.metrotest.model.Landmark

/**
 * Created by liteng on 10/13/17.
 */

class PersistanceManager(context: Context) {
    val sharedPreferences : SharedPreferences
    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun fetchFavorite() : List<Landmark> {
        var landmarkJson = sharedPreferences?.getString(Constants.LANDMARK_PREF_KEY, null)

        if(landmarkJson == null) {
            return arrayListOf<Landmark>()
        } else {
            val landmarkType = object :TypeToken<MutableList<Landmark>>(){}.type

            val landmark: List<Landmark> = Gson().fromJson(landmarkJson, landmarkType)

            return landmark.sortedByDescending { it.distance }
        }
    }

    fun saveFavorite(landmark:Landmark) {
        val landmarks = fetchFavorite().toMutableList()

        landmarks.add(landmark)

        val editor = sharedPreferences?.edit()
        editor?.putString(Constants.LANDMARK_PREF_KEY, Gson().toJson(landmarks))

        editor?.apply()
    }
}



