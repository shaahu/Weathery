package com.shahu.weathery.interface2

import com.android.volley.VolleyError
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONException

/**
 * Created by Shahu Ronghe on 23, September, 2019
 * in Weathery
 */
interface IVolleyResponse {
    fun onSuccessResponse(jsonObject: JsonObject?, requestType: String?)
    fun onRequestFailure(volleyError: VolleyError?, requestType: String?)

    @Throws(JSONException::class)
    fun onSuccessJsonArrayResponse(jsonObject: JSONArray?, requestType: String?)
    fun onStringSuccessRequest(response: String?, requestType: String?)
}