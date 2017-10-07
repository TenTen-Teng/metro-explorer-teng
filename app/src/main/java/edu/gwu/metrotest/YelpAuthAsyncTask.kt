package edu.gwu.metrotest

import android.content.Context
import android.util.Log
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion

/**
 * Created by liteng on 10/1/17.
 */

class YelpAuthAsyncTask (val context: Context){
    private val TAG = "YelpAuthAsyncTask"

    fun getYelpToken(){
        Ion.with(context)
                .load(Constants_yelp.YELP_TOKEN_URL)
                .setBodyParameter("grant_type", Constants_yelp.YELP_GRANT_TYPE)
                .setBodyParameter("client_id", Constants_yelp.YELP_CLIENT_ID)
                .setBodyParameter("client_secret", Constants_yelp.YELP_CLIENT_SECRET)
                .asString()
                .setCallback(FutureCallback{ error, result ->
                    error?.let {
                        //handle error situation
                        Log.e(TAG, it.toString())
                    }
                    result?.let {
                        //get token
                        if (it == null || it.length < 0) {
                            Log.v("accessToken is null", "null!")
                        } else {
                            Log.v("accessToken******", it)
                        }
                    }
                })

    }
}