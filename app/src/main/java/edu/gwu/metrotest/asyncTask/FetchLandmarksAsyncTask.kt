package edu.gwu.metrotest.asyncTask

import android.content.Context
import android.util.Log
import com.google.gson.JsonObject
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion
import edu.gwu.metrotest.Constants
import edu.gwu.metrotest.model.Landmark

/**
 * Created by liteng on 10/1/17.
 */

class FetchLandmarksAsyncTask(val context: Context){
    private val TAG = "FetchLandmarksAsyncTask"
    private var landmarks = ArrayList<Landmark>()
    private var yelpAuthAsyncTask : YelpAuthAsyncTask?= null
    val token = yelpAuthAsyncTask?.getYelpToken().toString()

    var itemSearchCompletionListener : ItemSearchCompletionListener?= null

    interface ItemSearchCompletionListener {
        fun landmarkItemLoaded(landmarks: ArrayList<Landmark>)
        fun landmarkItemNotLoaded()
    }

    //get landmark info from Yelp API
    fun loadLandmarkData(stationName:String) : ArrayList<Landmark>{
        Ion.with(context).load(Constants.YELP_SEARCH_URL)
                //.addHeader("Authorization", "Bearer " + token)
                .addHeader("Authorization", "Bearer " + Constants.YELP_TOKEN)
                .addQuery("term", "landmarks")
                .addQuery("location", stationName)
                .asJsonObject()
                .setCallback(FutureCallback{error, result ->
                    error?.let {
                        Log.e(TAG, it.toString())
                        itemSearchCompletionListener?.landmarkItemNotLoaded()
                    }
                    result?.let {
                        var itemsInfo = parseInfoFromYelpJSON(result, stationName)

                        if (itemsInfo != null && itemsInfo.size > 0) {
                            landmarks.sortBy { it.distance }

                            itemSearchCompletionListener?.landmarkItemLoaded(landmarks)
                        } else {
                            itemSearchCompletionListener?.landmarkItemNotLoaded()
                        }
                    }
                })
        return landmarks
    }

    //parse JSON
    fun parseInfoFromYelpJSON(jsonObject: JsonObject, stationName: String): ArrayList<Landmark>{
        val itemResults = jsonObject.getAsJsonArray("businesses")

        if (itemResults != null && itemResults.size() > 0) {
            for (i in 0..itemResults.size() - 1) {
                var itemResult = itemResults.get(i).asJsonObject
                var landmarkName = itemResult.get("name").toString().removeSurrounding("\"","\"")
                var landmarkImageUrl = itemResult.get("image_url").toString().removeSurrounding("\"","\"")
                var landmarkDistance = itemResult.get("distance").asInt
                var addressLine1 = ""
                var addressLine2 = ""

                var landmarkLocation = itemResult.get("location").asJsonObject
                var locations:JsonObject ?= landmarkLocation

                var displayAddress = locations?.get("display_address").toString()
                        .removeSurrounding("[","]")
                        .removeSurrounding("\"","\"")


                if (landmarkName == null && displayAddress == null && landmarkDistance == null) {
                    continue
                } else {
                    var addressLines = displayAddress.split("\",")

                    if (addressLines.size == 1) {
                        addressLine1 = addressLines[0]
                        addressLine2 = "-1"
                    } else {
                        if (addressLines.size == 2) {
                            addressLine1 = addressLines[0]
                            addressLine2 = addressLines[1]
                        }
                    }

                    if (landmarkImageUrl == null) {
                        landmarkImageUrl = "don't have image"
                    } else {
                        val mLandmark = Landmark(landmarkName, landmarkImageUrl, addressLine1, addressLine2,
                                landmarkDistance)
                        landmarks.add(mLandmark)
                    }
                }
            }
        } else {
            //no item situation
            Log.e("load error", "no item in the list")
        }
        return landmarks
    }
}