package com.shahu.weathery.common

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.shahu.weathery.common.Constants.API_BASE_URL
import com.shahu.weathery.common.Constants.API_GET_CITIES
import com.shahu.weathery.common.Constants.OPEN_WEATHER_MAP_API_KEY
import com.shahu.weathery.common.Constants.OPEN_WEATHER_MAP_BASE_URL
import com.shahu.weathery.common.Constants.WEATHER_BY_CITY_ID
import com.shahu.weathery.common.Constants.WEATHER_BY_COORDS_STRING
import com.shahu.weathery.common.Constants.WEATHER_FORECAST_BY_CITY_ID
import com.shahu.weathery.interface2.IVolleyResponse

/**
 * Created by Shahu Ronghe on 20, September, 2019
 * in Weathery
 */
class VolleyRequest(context: Context?, IVolleyResponse: IVolleyResponse?) {
    private val mRequestQueue: RequestQueue
    private val mIVolleyResponse: IVolleyResponse?
    fun getWeatherByCoords(requestType: String?, lon: Double, lat: Double) {
        val url: String = OPEN_WEATHER_MAP_BASE_URL + WEATHER_BY_COORDS_STRING + "lat=" + lat + "&lon=" + lon + OPEN_WEATHER_MAP_API_KEY
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url
                , null,
                Response.Listener { response ->
                    val jsonParser = JsonParser()
                    val `object` = jsonParser.parse(response.toString()) as JsonObject
                    mIVolleyResponse?.onSuccessResponse(`object`, requestType)
                    Log.e(TAG, "onResponse: $url")
                },
                Response.ErrorListener { error ->
                    Log.e(TAG, "onErrorResponse: $error")
                    mIVolleyResponse?.onRequestFailure(error, requestType)
                })
        mRequestQueue.add(jsonObjectRequest)
    }

    fun getWeatherByCityId(id: String, requestType: String?) {
        val url: String = OPEN_WEATHER_MAP_BASE_URL + WEATHER_BY_CITY_ID + id + OPEN_WEATHER_MAP_API_KEY
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url
                , null,
                Response.Listener { response ->
                    val jsonParser = JsonParser()
                    val `object` = jsonParser.parse(response.toString()) as JsonObject
                    mIVolleyResponse?.onSuccessResponse(`object`, requestType)
                },
                Response.ErrorListener { error ->
                    Log.e(TAG, "onErrorResponse: $error")
                    mIVolleyResponse?.onRequestFailure(error, requestType)
                })
        mRequestQueue.add(jsonObjectRequest)
    }

    fun getWeatherForecastByCityId(id: String, requestType: String?) {
        val url: String = OPEN_WEATHER_MAP_BASE_URL + WEATHER_FORECAST_BY_CITY_ID + id + OPEN_WEATHER_MAP_API_KEY
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url
                , null,
                Response.Listener { response ->
                    val jsonParser = JsonParser()
                    val `object` = jsonParser.parse(response.toString()) as JsonObject
                    mIVolleyResponse?.onSuccessResponse(`object`, requestType)
                },
                Response.ErrorListener { error ->
                    Log.e(TAG, "onErrorResponse: $error")
                    mIVolleyResponse?.onRequestFailure(error, requestType)
                })
        mRequestQueue.add(jsonObjectRequest)
    }

    fun getCitiesData(name: String, requestType: String?) {
        val url: String = API_BASE_URL + API_GET_CITIES + name
        val stringRequest = StringRequest(url, Response.Listener { response -> mIVolleyResponse?.onStringSuccessRequest(response, requestType) }, Response.ErrorListener { error -> Log.e(TAG, "onErrorResponse: ", error) })
        mRequestQueue.add(stringRequest)
    }

    companion object {
        private const val TAG = "VolleyRequest"
    }

    init {
        mRequestQueue = Volley.newRequestQueue(context)
        mIVolleyResponse = IVolleyResponse
    }
}