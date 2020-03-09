package com.shahu.weathery.interface2;

import com.android.volley.VolleyError;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Shahu Ronghe on 23, September, 2019
 * in Weathery
 */
public interface IVolleyResponse {

    public void onSuccessResponse(JsonObject jsonObject, String requestType);

    public void onRequestFailure(VolleyError volleyError, String requestType);

    public void onSuccessJsonArrayResponse(JSONArray jsonObject, String requestType) throws JSONException;
}
