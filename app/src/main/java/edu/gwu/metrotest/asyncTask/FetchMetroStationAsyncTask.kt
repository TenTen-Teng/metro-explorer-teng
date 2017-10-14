package edu.gwu.metrotest.asyncTask

import android.content.Context
import android.location.Location
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

    var itemsSearchCompletionListener : ItemsSearchCompletionListener ?= null
    var findStationNameListener : FindStationNameListener ?=null

    interface FindStationNameListener {
        fun stationNameFound(name: String)
        fun stationNameNotFound()
    }

    interface ItemsSearchCompletionListener {
        fun stationItemsLoaded(stations: ArrayList<MetroStation>)
        fun stationItemsNotLoaded()
    }

    fun loadStationData() : ArrayList<MetroStation>{
        Ion.with(context).load(Constants.WMATA_SEARCH_URL)
                .addHeader("api_key", Constants.WMATA_API_KEY)
                .asJsonObject()
                .setCallback(FutureCallback{ error, result ->
                    error?.let {
                        Log.e(TAG, it.message)
                        itemsSearchCompletionListener?.stationItemsNotLoaded()
                    }
                    result?.let {
                        val itemsInfo =  parseStationInfoFromWMATAJSON(result)
                        if (itemsInfo != null && itemsInfo.size > 0) {
                            itemsSearchCompletionListener?.stationItemsLoaded(stations)
                        } else {
                            itemsSearchCompletionListener?.stationItemsNotLoaded()
                        }
                    }
                })
        return stations
    }


    //fun findStationCode(loc : Location){
    fun findStationCode(lat: String, lon:String) {
        var stationCode : String

        Ion.with(context).load(Constants.WMATA_LOCATION_URL)
                .addHeader("api_key", Constants.WMATA_API_KEY)
                .addQuery("Lat", lat)
                .addQuery("Lon", lon)
                .addQuery("Radius", "800")
                .asJsonObject()
                .setCallback(FutureCallback{

                    error, result ->
                    error?.let {
                        Log.e(TAG, it.message)
                    }
                    result?.let {
                        stationCode =  parseLocationInfoFromWMATAJSON(result)

                        Log.e("stationCode", stationCode)
                        if (stationCode != "-1") {
                            findStationName(stationCode)//已知station code, 去找station name
                        } else {
                            //expand search radius * 2
                            Ion.with(context).load(Constants.WMATA_LOCATION_URL)
                                    .addHeader("api_key", Constants.WMATA_API_KEY)
                                    //.addQuery("Lat", loc.latitude.toString())
                                    //.addQuery("Lon", loc.longitude.toString())
                                    .addQuery("Lat", lat)
                                    .addQuery("Lon", lon)
                                    .addQuery("Radius", "1600")
                                    .asJsonObject()
                                    .setCallback(FutureCallback{ error, result ->
                                        error?.let {
                                            Log.e(TAG, it.message)
                                        }
                                        result?.let {
                                            stationCode =  parseLocationInfoFromWMATAJSON(result)

                                            if (stationCode != "-1") {
                                                findStationName(stationCode)//已知station code, 去找station name
                                            } else {
                                                //expand search radius * 2
                                                Ion.with(context).load(Constants.WMATA_LOCATION_URL)
                                                        .addHeader("api_key", Constants.WMATA_API_KEY)
                                                        //.addQuery("Lat", loc.latitude.toString())
                                                        //.addQuery("Lon", loc.longitude.toString())
                                                        .addQuery("Lat", lat)
                                                        .addQuery("Lon", lon)
                                                        .addQuery("Radius", "3200")
                                                        .asJsonObject()
                                                        .setCallback(FutureCallback{ error, result ->
                                                            error?.let {
                                                                Log.e(TAG, it.message)
                                                            }
                                                            result?.let {
                                                                stationCode =  parseLocationInfoFromWMATAJSON(result)

                                                                if (stationCode != "-1") {
                                                                    findStationName(stationCode)//已知station code, 去找station name
                                                                } else {
                                                                    //expand search radius * 2
                                                                    Ion.with(context).load(Constants.WMATA_LOCATION_URL)
                                                                            .addHeader("api_key", Constants.WMATA_API_KEY)
                                                                            //.addQuery("Lat", loc.latitude.toString())
                                                                            //.addQuery("Lon", loc.longitude.toString())
                                                                            .addQuery("Lat", lat)
                                                                            .addQuery("Lon", lon)
                                                                            .addQuery("Radius", "6400")
                                                                            .asJsonObject()
                                                                            .setCallback(FutureCallback{ error, result ->
                                                                                error?.let {
                                                                                    Log.e(TAG, it.message)
                                                                                }
                                                                                result?.let {
                                                                                    stationCode =  parseLocationInfoFromWMATAJSON(result)

                                                                                    if (stationCode != "-1") {
                                                                                        findStationName(stationCode)//已知station code, 去找station name
                                                                                    } else {
                                                                                        stationCode = "-1"
                                                                                    }
                                                                                }
                                                                            })
                                                                }
                                                            }
                                                        })
                                            }
                                        }
                                    })

                            Log.e(TAG, "can't find station code")
                        }
                    }
                })
    }

    //找location name
    fun findStationName(stationCode:String){
        Log.e("~~~~~~~~~~555555", "findStationName!!!!")

        if(stationCode == "-1") {
            findStationNameListener?.stationNameNotFound()
        } else {
            Ion.with(context).load(Constants.WMATA_INFO_URL)
                    .addHeader("api_key", Constants.WMATA_API_KEY)
                    .addQuery("StationCode", stationCode)
                    .asJsonObject()
                    //.asString()
                    .setCallback(FutureCallback{ error, result ->
                        error?.let {
                            Log.e(TAG, it.message)
                        }
                        result?.let {
                            //val stationName =  parseNameFromWMATAJSON(result)
                            val stationName = result.asJsonObject.get("Name").toString()

                            Log.e("stationName = ", stationName)

                            if (stationName != null && stationName.length > 0) {
                                findStationNameListener?.stationNameFound(stationName)
                            } else {
                                findStationNameListener?.stationNameNotFound()
                            }
                        }
                    })
        }

    }

    private fun parseStationInfoFromWMATAJSON(jsonObject: JsonObject): ArrayList<MetroStation>{
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
                    continue
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


    private fun parseLocationInfoFromWMATAJSON(jsonObject: JsonObject): String{
        Log.e("parseLocation!!!!", "parseLocationInfoFromWMATAJSON")
        var stationCode = "0"
        val locationResults = jsonObject.getAsJsonArray("Entrances")

        if (locationResults != null && locationResults.size() > 0) {
            for (i in 0..locationResults.size() - 1) {
                var locationResult = locationResults.get(i).asJsonObject

                stationCode = locationResult.get("StationCode1").toString().removeSurrounding("\"", "\"")

                if (stationCode == null) {
                    stationCode = "-1"
                }
            }
        } else {
            //no item situation
            stationCode = "-1"
            Log.e("load error", "no item in the list")
        }
        return stationCode
    }
}