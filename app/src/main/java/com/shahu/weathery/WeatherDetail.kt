package com.shahu.weathery

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.shahu.weathery.helper.ValuesConverter.getDateForCity
import com.shahu.weathery.helper.ValuesConverter.getTimeOnlyForCity
import kotlinx.android.synthetic.main.activity_weather_detail.*

class WeatherDetail : AppCompatActivity() {
    private var mCurrentTime: Long = 0
    private var mTimeZone = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_detail)

    }

    private fun inflateClock() {
        val someHandler = Handler(mainLooper)
        someHandler.postDelayed(object : Runnable {
            override fun run() {
                val date = getDateForCity(mCurrentTime, mTimeZone)
                val time = getTimeOnlyForCity(mCurrentTime, mTimeZone)
                main_date!!.text = date
                main_time!!.text = time
                mCurrentTime += 60
                someHandler.postDelayed(this, 60000)
            }
        }, 10)
    }

    companion object {
        private const val TAG = "WeatherDetail"
    }
}