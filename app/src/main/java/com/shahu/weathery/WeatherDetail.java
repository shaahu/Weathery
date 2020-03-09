package com.shahu.weathery;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shahu.weathery.common.VolleyRequest;
import com.shahu.weathery.customui.CitynameTextView;
import com.shahu.weathery.customui.TextHolderSubstanceCaps;
import com.shahu.weathery.helper.ImageHelper;
import com.shahu.weathery.helper.ValuesConverter;
import com.shahu.weathery.interface2.IVolleyResponse;
import com.shahu.weathery.model.CardModel;
import com.shahu.weathery.model.forecast.ForecastResponse;

import org.json.JSONArray;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.shahu.weathery.common.Constants.WEATHER_FORECAST_BY_ID_HTTP_REQUEST;


public class WeatherDetail extends AppCompatActivity {

    private static final String TAG = "WeatherDetail";
    private static String mCityId;
    private VolleyRequest mVolleyRequest;
    private IVolleyResponse mIVolleyResponseCallback = null;
    private CitynameTextView mCitynameTextView;
    private long mCurrentTime = 0;
    private int mTimeZone = 0;
    private String mDayNight, mTemperature, mDescription;
    private TextHolderSubstanceCaps mTextViewTime, mTextViewDate, mPressureTextView, mCloudinessTextView, mHumidityTextView;
    private TextView mDetailMainTemperatureTextView;
    private ImageView mDetailMainImageView;
    private TextView mFeelsLikeTextView, mDescriptionTextView;

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

            @Override
            public void onSuccessJsonArrayResponse(JSONArray jsonObject, String requestType) {

            }
        };
        mVolleyRequest = new VolleyRequest(this, mIVolleyResponseCallback);
    }

    private void viewsInitialization() {
        mCitynameTextView = findViewById(R.id.forecast_city);
        mTextViewDate = findViewById(R.id.main_date);
        mTextViewTime = findViewById(R.id.main_time);
        mDetailMainTemperatureTextView = findViewById(R.id.detail_main_temperature);
        mDetailMainImageView = findViewById(R.id.detail_main_image);
        mFeelsLikeTextView = findViewById(R.id.feels_like);
        mPressureTextView = findViewById(R.id.pressure);
        mCloudinessTextView = findViewById(R.id.sea_level);
        mHumidityTextView = findViewById(R.id.humidity);
        mDescriptionTextView = findViewById(R.id.description);
    }

    private void fetchCityWeatherData() {
        mCityId = getIntent().getStringExtra("id");
        mCurrentTime = getIntent().getLongExtra("time", 0);
        mDayNight = getIntent().getStringExtra("day");
        mTemperature = getIntent().getStringExtra("temperature");
        mDescription = getIntent().getStringExtra("desc");
        Log.d(TAG, "fetchCityWeatherData: " + mCityId);
        mVolleyRequest.getWeatherForecastByCityId(mCityId, WEATHER_FORECAST_BY_ID_HTTP_REQUEST);
    }

    @SuppressLint("SetTextI18n")
    private void weatherReqComplete(JsonObject jsonObject) {
        Gson gson = new Gson();
        ForecastResponse forecastResponse = gson.fromJson(jsonObject.toString(), ForecastResponse.class);
        mTimeZone = forecastResponse.getCity().getTimezone();
        inflateClock();
        mCitynameTextView.setText(forecastResponse.getCity().getName());
        CardModel cardModel = new CardModel();
        cardModel.setWeatherItem(forecastResponse.getList().get(0).getWeather().get(0));
        cardModel.setDayNight(mDayNight);
        try {
            mDetailMainImageView.setImageDrawable(ImageHelper.getDescriptionImageDrawable(cardModel, this));
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                CardModel cardModel1 = new CardModel();
                cardModel1.setDescription("na");
                try {
                    mDetailMainImageView.setImageDrawable(ImageHelper.getDescriptionImageDrawable(cardModel1, this));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
        }
        mDetailMainTemperatureTextView.setText(ValuesConverter.convertTemperatureToCelsius(mTemperature) + "\u00B0C");
        mFeelsLikeTextView.setText("feels like " + ValuesConverter.convertTemperatureToCelsius(String.valueOf(forecastResponse.getList().get(0).getMain().getFeelsLike())) + "\u00B0C");
        mPressureTextView.setText(forecastResponse.getList().get(0).getMain().getPressure() + " hPa");
        mHumidityTextView.setText(forecastResponse.getList().get(0).getMain().getHumidity() + " %");
        mCloudinessTextView.setText(forecastResponse.getList().get(0).getClouds().getAll() + " %");
        mDescriptionTextView.setText(mDescription);
    }

    private void inflateClock() {
        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final String date = ValuesConverter.getDateForCity(mCurrentTime, mTimeZone);
                final String time = ValuesConverter.getTimeOnlyForCity(mCurrentTime, mTimeZone);
                mTextViewDate.setText(date);
                mTextViewTime.setText(time);
                mCurrentTime = mCurrentTime + 60;
                someHandler.postDelayed(this, 60000);
            }
        }, 10);
    }
}
