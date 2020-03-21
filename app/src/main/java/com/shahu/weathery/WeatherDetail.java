package com.shahu.weathery;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shahu.weathery.common.Constants;
import com.shahu.weathery.common.VolleyRequest;
import com.shahu.weathery.customui.CitynameTextView;
import com.shahu.weathery.customui.TextHolderSubstanceCaps;
import com.shahu.weathery.helper.ImageHelper;
import com.shahu.weathery.helper.ValuesConverter;
import com.shahu.weathery.interface2.IVolleyResponse;
import com.shahu.weathery.model.CardModel;
import com.shahu.weathery.model.forecast.ForecastResponse;

import org.json.JSONArray;

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
    private SwipeRefreshLayout mPullToRefresh;
    private String mImageUrl;
    private String mCityName;
    private boolean mIsInternetAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);
        initSwipeRelativeRefreshLayout();
        volleyInitialization();
        viewsInitialization();
        fetchCityWeatherData();
    }

    private void initSwipeRelativeRefreshLayout() {
        mPullToRefresh = findViewById(R.id.pullToRefreshDetailed);
        mPullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchCityWeatherData();
                mPullToRefresh.setRefreshing(true);
            }
        });
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
            }

            @Override
            public void onSuccessJsonArrayResponse(JSONArray jsonObject, String requestType) {
            }

            @Override
            public void onStringSuccessRequest(String response, String requestType) {
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
        Bundle bundle = getIntent().getBundleExtra(Constants.BUNDLE_NAME);
        mCityId = bundle.getString(Constants.BUNDLE_CITY_ID);
        mCurrentTime = bundle.getLong(Constants.BUNDLE_TIME);
        mDayNight = bundle.getString(Constants.BUNDLE_DAY_NIGHT);
        mTemperature = bundle.getString(Constants.BUNDLE_TEMPERATURE);
        mDescription = bundle.getString(Constants.BUNDLE_DESCRIPTION);
        mImageUrl = bundle.getString(Constants.BUNDLE_IMAGE_URL);
        mCityName = bundle.getString(Constants.BUNDLE_CITY_NAME);
        mIsInternetAvailable = bundle.getBoolean(Constants.BUNDLE_INTERNET_AVAILABILITY);
        if (mIsInternetAvailable) {
            mPullToRefresh.setEnabled(true);
            mVolleyRequest.getWeatherForecastByCityId(mCityId, WEATHER_FORECAST_BY_ID_HTTP_REQUEST);
        } else {
            mPullToRefresh.setEnabled(false);
            mDetailMainTemperatureTextView.setText(ValuesConverter.convertTemperatureToCelsius(mTemperature) + "\u00B0C");
            Glide.with(this).load(mImageUrl).error(R.drawable.default_weather_icon).into(mDetailMainImageView);
            mCitynameTextView.setText(mCityName);
            mDescriptionTextView.setText(mDescription);
        }
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
        String iconUrl = ImageHelper.getDescriptionImageDrawable(cardModel);
        Glide.with(this).load(iconUrl).into(mDetailMainImageView);
        mDetailMainTemperatureTextView.setText(ValuesConverter.convertTemperatureToCelsius(mTemperature) + "\u00B0C");
        mFeelsLikeTextView.setText("feels like " + ValuesConverter.convertTemperatureToCelsius(String.valueOf(forecastResponse.getList().get(0).getMain().getFeelsLike())) + "\u00B0C");
        mPressureTextView.setText(forecastResponse.getList().get(0).getMain().getPressure() + " hPa");
        mHumidityTextView.setText(forecastResponse.getList().get(0).getMain().getHumidity() + " %");
        mCloudinessTextView.setText(forecastResponse.getList().get(0).getClouds().getAll() + " %");
        mDescriptionTextView.setText(mDescription);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullToRefresh.setRefreshing(false);

            }
        }, 800);
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
