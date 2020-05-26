package com.shahu.weathery

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.VolleyError
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.shahu.weathery.common.Constants
import com.shahu.weathery.common.Constants.WEATHER_FORECAST_BY_ID_HTTP_REQUEST
import com.shahu.weathery.common.VolleyRequest
import com.shahu.weathery.customui.CitynameTextView
import com.shahu.weathery.customui.TextHolderSubstanceCaps
import com.shahu.weathery.helper.ImageHelper.getDescriptionImageDrawable
import com.shahu.weathery.helper.ValuesConverter.convertTemperatureToCelsius
import com.shahu.weathery.helper.ValuesConverter.getDateForCity
import com.shahu.weathery.helper.ValuesConverter.getTimeOnlyForCity
import com.shahu.weathery.interface2.IVolleyResponse
import com.shahu.weathery.model.CardModel
import com.shahu.weathery.model.forecast.ForecastResponse
import kotlinx.android.synthetic.main.activity_weather_detail.*
import org.json.JSONArray

class WeatherDetail : AppCompatActivity() {
    private var mVolleyRequest: VolleyRequest? = null
    private var mIVolleyResponseCallback: IVolleyResponse? = null
    private var mCitynameTextView: CitynameTextView? = null
    private var mCurrentTime: Long = 0
    private var mTimeZone = 0
    private var mDayNight: String? = null
    private var mTemperature: String? = null
    private var mDescription: String? = null
    private var mTextViewTime: TextHolderSubstanceCaps? = null
    private var mTextViewDate: TextHolderSubstanceCaps? = null
    private var mDetailMainTemperatureTextView: TextView? = null
    private var mDetailMainImageView: ImageView? = null
    private var mFeelsLikeTextView: TextView? = null
    private var mDescriptionTextView: TextView? = null
    private var mImageUrl: String? = null
    private var mCityName: String? = null
    private var mIsInternetAvailable = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_detail)
        initSwipeRelativeRefreshLayout()
        volleyInitialization()
        viewsInitialization()
        fetchCityWeatherData()
    }

    private fun initSwipeRelativeRefreshLayout() {
        pullToRefreshDetailed.setOnRefreshListener {
            fetchCityWeatherData()
            pullToRefreshDetailed.isRefreshing = true
        }
    }

    private fun volleyInitialization() {
        mIVolleyResponseCallback = object : IVolleyResponse {
            override fun onSuccessResponse(jsonObject: JsonObject?, requestType: String?) {
                when (requestType) {
                    WEATHER_FORECAST_BY_ID_HTTP_REQUEST -> weatherReqComplete(jsonObject)
                }
            }

            override fun onRequestFailure(volleyError: VolleyError?, requestType: String?) {}
            override fun onSuccessJsonArrayResponse(jsonObject: JSONArray?, requestType: String?) {}
            override fun onStringSuccessRequest(response: String?, requestType: String?) {}
        }
        mVolleyRequest = VolleyRequest(this, mIVolleyResponseCallback)
    }

    private fun viewsInitialization() {
        mCitynameTextView = findViewById(R.id.forecast_city)
        mTextViewDate = findViewById(R.id.main_date)
        mTextViewTime = findViewById(R.id.main_time)
        mDetailMainTemperatureTextView = findViewById(R.id.detail_main_temperature)
        mDetailMainImageView = findViewById(R.id.detail_main_image)
        mFeelsLikeTextView = findViewById(R.id.feels_like)
        mDescriptionTextView = findViewById(R.id.description)
    }

    private fun fetchCityWeatherData() {
        val bundle = intent.getBundleExtra(Constants.BUNDLE_NAME)
        val mCityId = bundle.getString(Constants.BUNDLE_CITY_ID)
        mCurrentTime = bundle.getLong(Constants.BUNDLE_TIME)
        mDayNight = bundle.getString(Constants.BUNDLE_DAY_NIGHT)
        mTemperature = bundle.getString(Constants.BUNDLE_TEMPERATURE)
        mDescription = bundle.getString(Constants.BUNDLE_DESCRIPTION)
        mImageUrl = bundle.getString(Constants.BUNDLE_IMAGE_URL)
        mCityName = bundle.getString(Constants.BUNDLE_CITY_NAME)
        mIsInternetAvailable = bundle.getBoolean(Constants.BUNDLE_INTERNET_AVAILABILITY)
        if (mIsInternetAvailable) {
            pullToRefreshDetailed.isEnabled = true
            mVolleyRequest!!.getWeatherForecastByCityId(mCityId, WEATHER_FORECAST_BY_ID_HTTP_REQUEST)
        } else {
            pullToRefreshDetailed.isEnabled = false
            //mDetailMainTemperatureTextView!!.text = convertTemperatureToCelsius(mTemperature) + "\u00B0C"
            Glide.with(this).load(mImageUrl).error(R.drawable.default_weather_icon).into(mDetailMainImageView!!)
            mCitynameTextView!!.text = mCityName
            mDescriptionTextView!!.text = mDescription
        }
    }

    @SuppressLint("SetTextI18n")
    private fun weatherReqComplete(jsonObject: JsonObject?) {
        val gson = Gson()
        val forecastResponse = gson.fromJson(jsonObject.toString(), ForecastResponse::class.java)
        mTimeZone = forecastResponse.city!!.timezone
        inflateClock()
        mCitynameTextView!!.text = forecastResponse.city!!.name
        val cardModel = CardModel()
        cardModel.weatherItem = forecastResponse.list!![0].weather!![0]
        cardModel.dayNight = mDayNight
        val iconUrl = getDescriptionImageDrawable(cardModel)
        Glide.with(this).load(iconUrl).into(mDetailMainImageView!!)
        mDetailMainTemperatureTextView!!.text = convertTemperatureToCelsius(mTemperature!!) + "\u00B0C"
        mFeelsLikeTextView!!.text = "feels like " + convertTemperatureToCelsius(java.lang.String.valueOf(forecastResponse.list!![0].main!!.feelsLike)) + "\u00B0C"
        pressure.text = forecastResponse.list!![0].main!!.pressure.toString() + " hPa"
        humidity.text = forecastResponse.list!![0].main!!.humidity.toString() + " %"
        sea_level.text = forecastResponse.list!![0].clouds!!.all.toString() + " %"
        mDescriptionTextView!!.text = mDescription
        Handler().postDelayed({ pullToRefreshDetailed.isRefreshing = false }, 800)
    }

    private fun inflateClock() {
        val someHandler = Handler(mainLooper)
        someHandler.postDelayed(object : Runnable {
            override fun run() {
                val date = getDateForCity(mCurrentTime, mTimeZone)
                val time = getTimeOnlyForCity(mCurrentTime, mTimeZone)
                mTextViewDate!!.text = date
                mTextViewTime!!.text = time
                mCurrentTime += 60
                someHandler.postDelayed(this, 60000)
            }
        }, 10)
    }

    companion object {
        private const val TAG = "WeatherDetail"
    }
}