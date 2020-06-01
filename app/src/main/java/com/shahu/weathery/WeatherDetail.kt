package com.shahu.weathery

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shahu.weathery.adapter.DailyRecyclerViewAdapter
import com.shahu.weathery.adapter.HourlyRecyclerViewAdapter
import com.shahu.weathery.common.Constants
import com.shahu.weathery.helper.ValuesConverter.convertTemperatureToCelsius
import com.shahu.weathery.helper.ValuesConverter.getDayNight
import com.shahu.weathery.helper.ValuesConverter.getDayOfTheWeek
import com.shahu.weathery.helper.ValuesConverter.getHourOfTheDayByEpoch
import com.shahu.weathery.helper.ValuesConverter.getTimeOnlyForCity
import com.shahu.weathery.model.CardModel
import com.shahu.weathery.model.DailyModel
import com.shahu.weathery.model.HourlyModel
import com.shahu.weathery.model.onecall.Daily
import com.shahu.weathery.model.onecall.Hourly
import com.shahu.weathery.model.onecall.OneCallResponse
import com.shahu.weathery.retrofit.DataService
import com.shahu.weathery.retrofit.RetrofitInstance
import kotlinx.android.synthetic.main.activity_weather_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherDetail : AppCompatActivity() {
    private var mLat: String? = null
    private var mLon: String? = null
    private var hourlyRecyclerViewAdapter: HourlyRecyclerViewAdapter? = null
    private var dailyRecyclerViewAdapter: DailyRecyclerViewAdapter? = null
    private val mHourlyList = ArrayList<HourlyModel>()
    private val mDailyList = ArrayList<DailyModel>()
    private lateinit var mCityData: CardModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_detail)
        mCityData = intent.getParcelableExtra(Constants.CITY_CARD_MODEL)
        mLat = mCityData.lat
        mLon = mCityData.lon
        val service: DataService =
                RetrofitInstance(applicationContext).retrofitInstance.create(DataService::class.java)
        val call = service.oneCallForCity(mLat, mLon, "minutely", Constants.OPEN_WEATHER_MAP_API_KEY)
        call.enqueue(object : Callback<OneCallResponse> {
            override fun onFailure(call: Call<OneCallResponse>, t: Throwable) {
                Log.e(TAG, "on response failure", t)
            }

            override fun onResponse(call: Call<OneCallResponse>, response: Response<OneCallResponse>) {
                Log.d(TAG, response.body().toString())
                if (response.code() == 200) {
                    populateAllData(response.body())
                }
            }
        })
    }

    private fun populateAllData(oneCallResponse: OneCallResponse?) {
        main_city_name.text = mCityData.name
        description.text = oneCallResponse?.current!!.weather[0].description
        temperature.text = oneCallResponse.current.temp?.let { convertTemperatureToCelsius(it.toString()) } + "°"
        current_day.text = getDayOfTheWeek(oneCallResponse.current.dt, oneCallResponse.timezone_offset)
        current_day.visibility = View.VISIBLE
        current_max.text = mCityData.max?.let { convertTemperatureToCelsius(it) }
        current_min.text = mCityData.min?.let { convertTemperatureToCelsius(it) }
        populateHourlyData(oneCallResponse.hourly,
                oneCallResponse.current.sunrise,
                oneCallResponse.current.sunset,
                oneCallResponse.timezone_offset)

        populateDailyData(oneCallResponse.daily, oneCallResponse.timezone_offset)

        populateBottomData(oneCallResponse)
    }

    private fun populateHourlyData(hourly: List<Hourly>?, sunrise: Int?, sunset: Int?, timezoneOffset: Int?) {
        val sunriseHour = getHourOfTheDayByEpoch(sunrise!!.toLong(), timezoneOffset!!)
        val sunsetHour = getHourOfTheDayByEpoch(sunset!!.toLong(), timezoneOffset)
        for (item in hourly!!) {
            val currentHour = getHourOfTheDayByEpoch(item.dt.toLong(), timezoneOffset)
            val temperature = convertTemperatureToCelsius(item.temp.toString())
            val timeOfDay = getDayNight(timezoneOffset, sunrise, sunset, item.dt)
            mHourlyList.add(HourlyModel(currentHour!!,
                    temperature,
                    item.weather[0].main,
                    item.weather[0].description,
                    timeOfDay))
            if (sunriseHour!!.toInt() == currentHour.toInt()) {
                val sunriseWithMin = getTimeOnlyForCity(sunrise.toLong(), timezoneOffset)
                mHourlyList.add(HourlyModel(sunriseWithMin,
                        temperature,
                        "inva",
                        "inva",
                        "sunrise"))
            }
            if (sunsetHour!!.toInt() == currentHour.toInt()) {
                val sunsetWithMin = getTimeOnlyForCity(sunset.toLong(), timezoneOffset)
                mHourlyList.add(HourlyModel(sunsetWithMin,
                        temperature,
                        "inva",
                        "inva",
                        "sunset"))
            }
            if (mHourlyList.size == 26)
                break
        }
        Log.d(TAG, "final list: ${mHourlyList.toArray()}")

        hourlyRecyclerViewAdapter =
                HourlyRecyclerViewAdapter(mHourlyList, this)
        val layoutManager: RecyclerView.LayoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        hour_forecast_recycler_view.layoutManager = layoutManager
        hour_forecast_recycler_view.adapter = hourlyRecyclerViewAdapter
    }

    private fun populateDailyData(daily: List<Daily>, timezoneOffset: Int) {
        for (item in daily) {
            val day = getDayOfTheWeek(item.dt, timezoneOffset)
            val max = convertTemperatureToCelsius(item.temp.max.toString())
            val min = convertTemperatureToCelsius(item.temp.min.toString())
            mDailyList.add(DailyModel(day!!, item.weather[0].main, item.weather[0].description, max, min))
        }

        dailyRecyclerViewAdapter =
                DailyRecyclerViewAdapter(mDailyList, this)
        val layoutManager: RecyclerView.LayoutManager =
                LinearLayoutManager(this)
        daily_forecast_recycler_view.layoutManager = layoutManager
        daily_forecast_recycler_view.adapter = dailyRecyclerViewAdapter
    }

    private fun populateBottomData(oneCallResponse: OneCallResponse) {
        sunriseTV.text =
                getTimeOnlyForCity(oneCallResponse.current.sunrise.toLong(), oneCallResponse.timezone_offset)

        sunsetTV.text =
                getTimeOnlyForCity(oneCallResponse.current.sunset.toLong(), oneCallResponse.timezone_offset)

        cloudTV.text = oneCallResponse.current.clouds.toString() + "%"

        humidityTV.text = oneCallResponse.current.humidity.toString() + "%"

        windTV.text = oneCallResponse.current.wind_speed.toString()

        feelsLikeTV.text = convertTemperatureToCelsius(oneCallResponse.current.feels_like.toString()) + "°"

        pressureTV.text = oneCallResponse.current.pressure.toString() + " hPa"

        uvIndexTV.text = oneCallResponse.current.uvi.toString()

        bottom_layout.visibility = View.VISIBLE
    }

    companion object {
        private const val TAG = "WeatherDetail"
    }
}