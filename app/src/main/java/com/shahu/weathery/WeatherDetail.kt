package com.shahu.weathery

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.shahu.weathery.customui.CitynameTextView
import com.shahu.weathery.customui.TextHolderSubstanceCaps
import com.shahu.weathery.helper.ValuesConverter.getDateForCity
import com.shahu.weathery.helper.ValuesConverter.getTimeOnlyForCity
import com.shahu.weathery.model.forecast.ForecastResponse

class WeatherDetail : AppCompatActivity() {
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
        viewsInitialization()
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

    @SuppressLint("SetTextI18n")
    private fun weatherReqComplete(jsonObject: JsonObject?) {
        val gson = Gson()
        val forecastResponse = gson.fromJson(jsonObject.toString(), ForecastResponse::class.java)
        mTimeZone = forecastResponse.city!!.timezone
        inflateClock()

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