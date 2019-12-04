package com.shahu.weathery.common;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shahu.weathery.interface2.IVolleyResponse;

import static com.shahu.weathery.common.Constants.OPEN_WEATHER_MAP_API_KEY;
import static com.shahu.weathery.common.Constants.OPEN_WEATHER_MAP_BASE_URL;
import static com.shahu.weathery.common.Constants.WEATHER_BY_CITY_ID;
import static com.shahu.weathery.common.Constants.WEATHER_BY_COORDS_STRING;
import static com.shahu.weathery.common.Constants.WEATHER_FORECAST_BY_CITY_ID;

/**
 * Created by Shahu Ronghe on 20, September, 2019
 * in Weathery
 */
public class VolleyRequest {
    private static final String TAG = "VolleyRequest";
    private RequestQueue mRequestQueue;
    private IVolleyResponse mIVolleyResponse;

    public VolleyRequest(Context context, IVolleyResponse IVolleyResponse) {
        mRequestQueue = Volley.newRequestQueue(context);
        mIVolleyResponse = IVolleyResponse;
    }

    public void getWeatherByCoords(final String requestType, double lon, double lat) {
        final String url = OPEN_WEATHER_MAP_BASE_URL + WEATHER_BY_COORDS_STRING + "lat=" + lat + "&lon=" + lon + OPEN_WEATHER_MAP_API_KEY;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url
                , null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        JsonParser jsonParser = new JsonParser();
                        JsonObject object = (JsonObject) jsonParser.parse(response.toString());
                        if (mIVolleyResponse != null)
                            mIVolleyResponse.onSuccessResponse(object, requestType);
                        Log.e(TAG, "onResponse: " + url);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: " + error);
                        if (mIVolleyResponse != null)
                            mIVolleyResponse.onRequestFailure(error, requestType);
                    }
                });


        mRequestQueue.add(jsonObjectRequest);
    }

    public void getWeatherByCityId(final String id, final String requestType) {
        final String url = OPEN_WEATHER_MAP_BASE_URL + WEATHER_BY_CITY_ID + id + OPEN_WEATHER_MAP_API_KEY;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url
                , null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        JsonParser jsonParser = new JsonParser();
                        JsonObject object = (JsonObject) jsonParser.parse(response.toString());
                        if (mIVolleyResponse != null)
                            mIVolleyResponse.onSuccessResponse(object, requestType);
                        Log.e(TAG, "onResponse: " + url);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: " + error);
                        if (mIVolleyResponse != null)
                            mIVolleyResponse.onRequestFailure(error, requestType);
                    }
                });


        mRequestQueue.add(jsonObjectRequest);
    }

    public void getWeatherForecastByCityId(String id, final String requestType) {
        final String url = OPEN_WEATHER_MAP_BASE_URL + WEATHER_FORECAST_BY_CITY_ID + id + OPEN_WEATHER_MAP_API_KEY;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url
                , null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        JsonParser jsonParser = new JsonParser();
                        JsonObject object = (JsonObject) jsonParser.parse(response.toString());
                        if (mIVolleyResponse != null)
                            mIVolleyResponse.onSuccessResponse(object, requestType);
                        Log.e(TAG, "onResponse: " + url);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: " + error);
                        if (mIVolleyResponse != null)
                            mIVolleyResponse.onRequestFailure(error, requestType);
                    }
                });


        mRequestQueue.add(jsonObjectRequest);
    }
}
