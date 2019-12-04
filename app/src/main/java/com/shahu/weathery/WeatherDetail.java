package com.shahu.weathery;


import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shahu.weathery.common.VolleyRequest;
import com.shahu.weathery.customui.CitynameTextView;
import com.shahu.weathery.interface2.IVolleyResponse;
import com.shahu.weathery.model.forecast.ForecastResponse;

import static com.shahu.weathery.common.Constants.WEATHER_BY_ID_HTTP_REQUEST;
import static com.shahu.weathery.common.Constants.WEATHER_FORECAST_BY_ID_HTTP_REQUEST;


public class WeatherDetail extends AppCompatActivity {

    private static final String TAG = "WeatherDetail";
    private static String mCityId;
    private VolleyRequest mVolleyRequest;
    private IVolleyResponse mIVolleyResponseCallback = null;
    private CitynameTextView mCitynameTextView;

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
                    case WEATHER_FORECAST_BY_ID_HTTP_REQUEST:
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
        mCitynameTextView = findViewById(R.id.forecast_city);
    }

    private void fetchCityWeatherData() {
        mCityId = getIntent().getStringExtra("id");
        Log.d(TAG, "fetchCityWeatherData: " + mCityId);
        mVolleyRequest.getWeatherForecastByCityId(mCityId, WEATHER_FORECAST_BY_ID_HTTP_REQUEST);
    }

    private void weatherReqComplete(JsonObject jsonObject) {
        Gson gson = new Gson();
        ForecastResponse forecastResponse = gson.fromJson(jsonObject.toString(), ForecastResponse.class);
        mCitynameTextView.setText(forecastResponse.getCity().getName());
    }
}
