package com.shahu.weathery;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shahu.weathery.common.VolleyRequest;
import com.shahu.weathery.interface2.IVolleyResponse;
import com.shahu.weathery.model.OpenWeatherMainResponse;

import static com.shahu.weathery.common.Constants.WEATHER_HTTP_REQUEST_BY_ID;


public class WeatherDetail extends AppCompatActivity {

    private static final String TAG = "WeatherDetail";
    private static String mCityId;
    private CardView mLoadingBoxCardView;
    private VolleyRequest mVolleyRequest;
    private IVolleyResponse mIVolleyResponseCallback = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);
        volleyInitialization();
        viewsInitialization();
        fetchCityWeatherData();
    }

    private void volleyInitialization() {
        mIVolleyResponseCallback = new IVolleyResponse() {
            @Override
            public void onSuccessResponse(JsonObject jsonObject, String requestType) {
                switch (requestType) {
                    case WEATHER_HTTP_REQUEST_BY_ID:
                        weatherReqComplete(jsonObject);
                }
            }

            @Override
            public void onRequestFailure(VolleyError volleyError, String requestType) {
                Log.d(TAG, "onRequestFailure: " + volleyError.getMessage());
            }
        };

        mVolleyRequest = new VolleyRequest(this, mIVolleyResponseCallback);
    }

    private void viewsInitialization() {
        mLoadingBoxCardView = findViewById(R.id.cv_loading);
    }

    private void fetchCityWeatherData() {
        mCityId = getIntent().getStringExtra("id");
        Log.d(TAG, "fetchCityWeatherData: " + mCityId);
        mLoadingBoxCardView.setVisibility(View.VISIBLE);
        mVolleyRequest.getWeatherByCityId(mCityId, WEATHER_HTTP_REQUEST_BY_ID);
    }

    private void weatherReqComplete(JsonObject jsonObject) {
        Gson gson = new Gson();
        OpenWeatherMainResponse openWeatherMainResponse = gson.fromJson(jsonObject.toString(), OpenWeatherMainResponse.class);
        allRequestCompletes();
    }

    private void allRequestCompletes() {
        mLoadingBoxCardView.setVisibility(View.GONE);
    }
}
