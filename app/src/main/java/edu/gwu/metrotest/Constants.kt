package edu.gwu.metrotest

/**
 * Created by liteng on 10/8/17.
 */

object Constants {
    val YELP_TOKEN_URL = "https://api.yelp.com/oauth2/token"
    val YELP_SEARCH_URL = "https://api.yelp.com/v3/businesses/search?"
    val YELP_CLIENT_ID = "Wg5gofjuRwnWC1a-CoaCxQ"
    val YELP_CLIENT_SECRET = "SCKvbrgmth8eKxmXuM777K7hFYKQ46C23solWGs36UIpUCeST34RCtTuBWNZLhyB"
    val YELP_GRANT_TYPE = "client_credentials"
    val YELP_TOKEN = "0wbKQw08FNn3MHBq7mqcoDzqKeGfpbmSpmdwctzoGvd7xOMOfAZIz6Uuz3_PTgfsMIM0HmiNtifd-Xlxd9IRqllSdNJZOg0gAYifejVDDgELaaQvxk6st84pgErRWXYx"

    //url for finding station list
    val WMATA_SEARCH_URL = "https://api.wmata.com/Rail.svc/json/jStations"
    //url for finding station name according to station station code
    val WMATA_INFO_URL = "https://api.wmata.com/Rail.svc/json/jStationInfo"
    //url for finding station code according to location
    val WMATA_LOCATION_URL = "https://api.wmata.com/Rail.svc/json/jStationEntrances"
    val WMATA_API_KEY = "4e7abbfe86004387888ba5f9bc9aa769"

    val LANDMARK_PREF_KEY = "LANDMARK"
}