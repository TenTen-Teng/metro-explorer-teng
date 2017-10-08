package edu.gwu.metrotest.asyncTask

import android.content.Context
import android.util.Log
import com.google.gson.JsonObject
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion
import edu.gwu.metrotest.Constants
import edu.gwu.metrotest.model.MetroStation

/**
 * Created by liteng on 10/8/17.
 */

class FetchMetroStationAsyncTask (val context: Context) {
    private val TAG = "FetchMetroAsyncTask"
    private var stations = ArrayList<MetroStation>()

    var itemSearchCompletionListener : ItemSearchCompletionListener?= null

    interface ItemSearchCompletionListener {
        fun stationItemLoaded(stations: ArrayList<MetroStation>)
        fun stationItemNotLoaded()
    }

    fun loadStationData() : ArrayList<MetroStation>{
        Ion.with(context).load(Constants.WMATA_SEARCH_URL)
                .addHeader("api_key", "4e7abbfe86004387888ba5f9bc9aa769")
                .asJsonObject()
                .setCallback(FutureCallback{ error, result ->
                    error?.let {
                        Log.e(TAG, it.message)
                        itemSearchCompletionListener?.stationItemNotLoaded()
                    }
                    result?.let {
                        val itemsInfo =  parseInfoFromWMATAJSON(result)
                        if (itemsInfo != null && itemsInfo.size > 0) {
                            itemSearchCompletionListener?.stationItemLoaded(stations)
                        } else {
                            itemSearchCompletionListener?.stationItemNotLoaded()
                        }
                    }
                })
        return stations
    }

    fun parseInfoFromWMATAJSON(jsonObject: JsonObject): ArrayList<MetroStation>{
        val itemResults = jsonObject.getAsJsonArray("Stations")

        if (itemResults != null && itemResults.size() > 0) {
            for (i in 0..itemResults.size() - 1) {
                var itemResult = itemResults.get(i).asJsonObject

                var name = itemResult.get("Name").toString().removeSurrounding("\"","\"")
                var lat = itemResult.get("Lat").toString().removeSurrounding("\"","\"")
                var lon = itemResult.get("Lon").toString().removeSurrounding("\"","\"")
                var lineCode1 = itemResult.get("LineCode1").toString().removeSurrounding("\"","\"")
                var lineCode2 = itemResult.get("LineCode2").toString().removeSurrounding("\"","\"")
                var lineCode3 = itemResult.get("LineCode3").toString().removeSurrounding("\"","\"")

                var stationAddress = itemResult.get("Address").asJsonObject
                var addresses: JsonObject?= stationAddress

                var street = addresses?.get("Street").toString()
                var city = addresses?.get("City").toString()
                var state = addresses?.get("State").toString()
                var zip = addresses?.get("Zip").toString()

                if (name == null || addresses == null) {
                    continue;
                } else {
                    val mStation = MetroStation(name, street, lat, lon, lineCode1, lineCode2, lineCode3)
                    stations.add(mStation)
                }
            }
        } else {
            //no item situation
            Log.e("load error", "no item in the list")
        }
        return stations
    }
}