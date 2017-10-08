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

    fun loadLandmarkData() : ArrayList<Landmark>{
        Ion.with(context).load(Constants.YELP_SEARCH_URL)
                //.addHeader("Authorization", "Bearer " + token.toString())
                .addHeader("Authorization", "Bearer " + Constants.YELP_TOKEN)
                .addQuery("term", "landmarks")
                //.addQuery("latitude", lat)
                //.addQuery("longitude", lon)
                .addQuery("location", "virgina")
                //.addQuery("Authorization", "Bearer " + token.toString())
                //.addQuery("Authorization", "Bearer " + Constants_yelp.YELP_TOKEN)
                .asJsonObject()
                .setCallback(FutureCallback{error, result ->
                    error?.let {
                        Log.e(TAG, it.toString())
                        itemSearchCompletionListener?.landmarkItemNotLoaded()
                    }
                    result?.let {
                        var itemsInfo = parseInfoFromYelpJSON(result)

                        if (itemsInfo != null && itemsInfo.size > 0) {
                            itemSearchCompletionListener?.landmarkItemLoaded(landmarks)
                        } else {
                            itemSearchCompletionListener?.landmarkItemNotLoaded()
                        }
                    }
                })

        return landmarks
    }

    fun parseInfoFromYelpJSON(jsonObject: JsonObject): ArrayList<Landmark>{
        val itemResults = jsonObject.getAsJsonArray("businesses")

        if (itemResults != null && itemResults.size() > 0) {
            for (i in 0..itemResults.size() - 1) {
                var itemResult = itemResults.get(i).asJsonObject

                var landmarkName = itemResult.get("name").toString().removeSurrounding("\"","\"")
                var landmarkImageUrl = itemResult.get("image_url").toString().removeSurrounding("\"","\"")
                var landmarkDistance = itemResult.get("distance").asInt

                var landmarklocation = itemResult.get("location").asJsonObject
                var locations:JsonObject ?= landmarklocation

                var address1 = locations?.get("address1").toString()
                var address2 = locations?.get("address2").toString()
                var address3 = locations?.get("address3").toString()
                var city = locations?.get("city").toString()
                var zipCode = locations?.get("zip_code").toString()
                var country = locations?.get("country").toString()
                var state = locations?.get("state").toString()


                var displayAddress = locations?.get("display_address").toString()
                        .removeSurrounding("[","]")
                var displayAddressLine1 = displayAddress.split("\",").get(0)
                var displayAddressLine2 = displayAddress.split("\",").get(1)


                if (landmarkName == null || displayAddressLine1 == null || landmarkDistance == null) {
                    continue;
                } else {
                    if (landmarkImageUrl == null) {
                        landmarkImageUrl = "don't have image"
                    } else {
                        val mLandmark = Landmark(landmarkName, landmarkImageUrl, displayAddressLine1, displayAddressLine2, landmarkDistance)
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