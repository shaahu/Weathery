package com.shahu.weathery

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.shahu.weathery.adapter.LocationRecyclerViewAdapter
import com.shahu.weathery.adapter.LocationRecyclerViewAdapter.MyViewHolder
import com.shahu.weathery.common.Constants
import com.shahu.weathery.common.LocationSharedPreferences
import com.shahu.weathery.customui.CustomSearchDialog
import com.shahu.weathery.helper.ConnectivityReceiver
import com.shahu.weathery.helper.Locator
import com.shahu.weathery.helper.RecyclerViewItemHelper
import com.shahu.weathery.helper.ValuesConverter.getDayNight
import com.shahu.weathery.interface2.IRecyclerViewListener
import com.shahu.weathery.interface2.OnDragListener
import com.shahu.weathery.interface2.OnSearchItemSelection
import com.shahu.weathery.interface2.OnSwipeListener
import com.shahu.weathery.model.CardModel
import com.shahu.weathery.model.main.MainResponse
import com.shahu.weathery.retrofit.DataService
import com.shahu.weathery.retrofit.RetrofitInstance
import kotlinx.android.synthetic.main.activity_main.*
import net.danlew.android.joda.JodaTimeAndroid
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {
    private var mRecyclerViewListener: IRecyclerViewListener? = null
    private var mLocationSharedPreferences: LocationSharedPreferences? = null
    private var mCityName: TextView? = null
    private val mCardModelArrayList = ArrayList<CardModel>()
    private var mLocationRecyclerViewAdapter: LocationRecyclerViewAdapter? = null
    private var mCurrentLocationCityId: String? = null
    private val mCurrentLocationDefaultCityId = "001"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialization()
    }

    /**
     * initialize the variables.
     */
    private fun initialization() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        JodaTimeAndroid.init(this)
        mCityName = findViewById(R.id.main_city_name)
        mLocationSharedPreferences = LocationSharedPreferences(this)
        add_new_loc_btn.setOnClickListener { searchForNewLocation() }
        if (!setCurrentCoordinates()) {
            fetchAllData(mLocationSharedPreferences!!.allLocations)
        }
        initRecyclerView()
        initPullToRefresh()
    }

    private fun initPullToRefresh() {
        pullToRefresh.setOnRefreshListener {
            if (!setCurrentCoordinates()) {
                fetchAllData(mLocationSharedPreferences!!.allLocations)
            }
            pullToRefresh.isRefreshing = true
        }
    }

    /**
     * Search engine from web api.
     */
    private fun searchForNewLocation() {
        val customSearchDialog = CustomSearchDialog(this, this, ArrayList())
        customSearchDialog.show()
        customSearchDialog.setOnItemSelected(object : OnSearchItemSelection {
            override fun onClick(cityId: String?) {
                if (mLocationSharedPreferences!!.addNewLocation(cityId)) {
                    val service: DataService = RetrofitInstance(applicationContext).retrofitInstance.create(DataService::class.java)
                    val call: Call<JsonObject> =
                            service.getWeatherByCityId(cityId,
                                    Constants.OPEN_WEATHER_MAP_API_KEY)
                    call.enqueue(object : Callback<JsonObject> {
                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                            Log.e(TAG, "request onFailure", t)
                        }

                        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                            if (response.code() == 200)
                                addFavouriteCityWeather(response.body())
                        }

                    })
                } else {
                    Toast.makeText(this@MainActivity, "Already Exist", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    /**
     * Method to set the weather of other favourites location.
     *
     * @param jsonObject data
     */
    private fun addFavouriteCityWeather(jsonObject: JsonObject?) {
        val gson = Gson()
        val mainResponse = gson.fromJson(jsonObject.toString(), MainResponse::class.java)
        val cardModel = CardModel()
        var cityName = mainResponse.name
        if (cityName!!.length > 16) {
            cityName = cityName.substring(0, 16) + "..."
        }
        cardModel.name = cityName
        cardModel.lon = mainResponse.coord?.lon.toString()
        cardModel.lat = mainResponse.coord?.lat.toString()
        cardModel.max = mainResponse.main?.tempMax.toString()
        cardModel.min = mainResponse.main?.tempMin.toString()
        cardModel.countryCode = mainResponse.sys!!.country
        cardModel.position =
                mLocationSharedPreferences!!.getPositionByCityId(java.lang.String.valueOf(mainResponse.id))!!.toInt()
        cardModel.temperature = java.lang.String.valueOf(mainResponse.main!!.temp)
        cardModel.time = mainResponse.dt.toLong()
        cardModel.secondsShift = mainResponse.timezone
        cardModel.weatherItem = mainResponse.weather!![0]
        cardModel.description = mainResponse.weather!![0].description!!.toUpperCase(Locale.ROOT)
        cardModel.cityId = java.lang.String.valueOf(mainResponse.id)
        cardModel.dayNight =
                getDayNight(mainResponse.timezone,
                        mainResponse.sys!!.sunrise,
                        mainResponse.sys!!.sunset,
                        mainResponse.dt)
        val iterator = mCardModelArrayList.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().cityId == cardModel.cityId) {
                iterator.remove()
            }
        }
        mCardModelArrayList.add(cardModel)
        mCardModelArrayList.sortWith(Comparator { o1, o2 -> o1.position - o2.position })
        mLocationRecyclerViewAdapter!!.notifyDataSetChanged()
    }

    /**
     * Method to add current location plus city name to header.
     *
     * @param jsonObject data
     */
    private fun addCurrentLocationData(jsonObject: JsonObject?) {
        val gson = Gson()
        val mainResponse = gson.fromJson(jsonObject.toString(), MainResponse::class.java)
        val cardModel = CardModel()
        cardModel.name = "Current Location"
        cardModel.countryCode = mainResponse.sys!!.country
        cardModel.position = 0
        cardModel.lon = mainResponse.coord?.lon.toString()
        cardModel.lat = mainResponse.coord?.lat.toString()
        cardModel.cityId = mCurrentLocationDefaultCityId
        mCurrentLocationCityId = java.lang.String.valueOf(mainResponse.id)
        cardModel.temperature = java.lang.String.valueOf(mainResponse.main!!.temp)
        cardModel.weatherItem = mainResponse.weather!![0]
        cardModel.dayNight = getDayNight(mainResponse.timezone,
                mainResponse.sys!!.sunrise,
                mainResponse.sys!!.sunset,
                mainResponse.dt)
        cardModel.description = mainResponse.weather!![0].description
        val iterator = mCardModelArrayList.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().cityId == cardModel.cityId) {
                iterator.remove()
            }
        }
        mCardModelArrayList.add(cardModel)
        mLocationRecyclerViewAdapter!!.notifyDataSetChanged()
        mCityName!!.text = String.format("%s, %s", mainResponse.name, mainResponse.sys!!.country)
    }

    /**
     * Method to fetch all stored location.
     *
     * @param allLocations contain all saved location in sharedPreference.
     */
    private fun fetchAllData(allLocations: Map<String, *>) {
        Log.d(TAG, "fetchAllData: $allLocations")
        val service: DataService = RetrofitInstance(applicationContext).retrofitInstance.create(DataService::class.java)
        for ((_, value) in allLocations) {
            val call: Call<JsonObject> =
                    service.getWeatherByCityId(value.toString(),
                            Constants.OPEN_WEATHER_MAP_API_KEY)
            call.enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e(TAG, "request onFailure", t)
                    Handler().postDelayed({ pullToRefresh.isRefreshing = false }, 2000)
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Handler().postDelayed({ pullToRefresh.isRefreshing = false }, 2000)
                    if (response.code() == 200)
                        addFavouriteCityWeather(response.body())
                }
            })
        }
    }

    /**
     * Method to set current location coordinates.
     */
    private fun setCurrentCoordinates(): Boolean {
        val retValue = booleanArrayOf(false)
        //TODO: check for image here
        val locationHelper = Locator(this)
        locationHelper.getLocation(Locator.Method.NETWORK_THEN_GPS, object : Locator.Listener {
            override fun onLocationFound(location: Location?) {
                retValue[0] = true
                val service: DataService = RetrofitInstance(applicationContext).retrofitInstance.create(DataService::class.java)
                val call: Call<JsonObject> =
                        service.getWeatherByLocation(location?.latitude.toString(),
                                location?.longitude.toString(),
                                Constants.OPEN_WEATHER_MAP_API_KEY)

                call.enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        Log.d(TAG, "onResponse: " + response.body().toString())
                        if (response.code() == 200) {
                            addCurrentLocationData(response.body())
                            fetchAllData(mLocationSharedPreferences!!.allLocations)
                        }
                        Handler().postDelayed({ pullToRefresh.isRefreshing = false }, 2000)
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        Log.e(TAG, "request onFailure", t)
                        Handler().postDelayed({ pullToRefresh.isRefreshing = false }, 2000)
                    }
                })
            }

            override fun onLocationNotFound() {
                mCityName!!.text = "Location not found!"
                fetchAllData(mLocationSharedPreferences!!.allLocations)
                Log.e(TAG, "onLocationNotFound: ")
            }
        })
        return retValue[0]
    }

    /**
     * initialize the recycler view.
     */
    private fun initRecyclerView() {
        mRecyclerViewListener = object : IRecyclerViewListener {
            override fun onSingleShortClickListener(cityId: String?) {
                val intent = Intent(applicationContext, WeatherDetail::class.java)
                val selectedCity: CardModel = getCityModelById(cityId)
                intent.putExtra(Constants.CITY_CARD_MODEL, selectedCity)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }

        mLocationRecyclerViewAdapter =
                LocationRecyclerViewAdapter(mCardModelArrayList, this, mRecyclerViewListener)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        locations.layoutManager = layoutManager
        locations.adapter = mLocationRecyclerViewAdapter

        val touchHelper =
                RecyclerViewItemHelper<CardModel>(mCardModelArrayList,
                        mLocationRecyclerViewAdapter as RecyclerView.Adapter<*>?)
        touchHelper.setRecyclerItemDragEnabled(true).onDragItemListener = object : OnDragListener {
            override fun onDragItemListener(fromPosition: Int, toPosition: Int) {
                Log.d(TAG, "onDragItemListener: from: $fromPosition, to: $toPosition")
                mLocationSharedPreferences!!.updatePosition(fromPosition, toPosition)
            }
        }

        touchHelper.setRecyclerItemSwipeEnabled(true).onSwipeItemListener = object : OnSwipeListener {
            override fun onSwipeItemListener(oldPosition: ViewHolder?) {
                val myViewHolder = oldPosition as MyViewHolder?
                if (myViewHolder != null) {
                    Log.d(TAG, "onSwipeItemListener: remove location: " +
                            mLocationSharedPreferences!!.removeLocation(myViewHolder.cityId))
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(touchHelper)
        itemTouchHelper.attachToRecyclerView(locations)
    }

    companion object {
        private const val TAG = "MainActivity"
    }

    private fun getCityModelById(id: String?): CardModel {
        for (item in mCardModelArrayList) {
            if (item.cityId == id)
                return item
        }
        return CardModel()
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {

    }

    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {

    }
}