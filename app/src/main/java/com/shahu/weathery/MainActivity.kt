package com.shahu.weathery

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.android.volley.VolleyError
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.shahu.weathery.adapter.LocationRecyclerViewAdapter
import com.shahu.weathery.adapter.LocationRecyclerViewAdapter.MyViewHolder
import com.shahu.weathery.common.Constants
import com.shahu.weathery.common.Constants.CURRENT_LOCATION_HTTP_REQUEST
import com.shahu.weathery.common.Constants.WEATHER_BY_ID_HTTP_REQUEST
import com.shahu.weathery.common.LocationSharedPreferences
import com.shahu.weathery.common.OfflineDataSharedPreference
import com.shahu.weathery.common.VolleyRequest
import com.shahu.weathery.customui.CustomSearchDialog
import com.shahu.weathery.helper.Locator
import com.shahu.weathery.helper.RecyclerViewItemHelper
import com.shahu.weathery.helper.ValuesConverter.getDayNight
import com.shahu.weathery.interface2.*
import com.shahu.weathery.model.CardModel
import com.shahu.weathery.model.common.MainResponse
import kotlinx.android.synthetic.main.activity_main.*
import net.danlew.android.joda.JodaTimeAndroid
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    var recyclerViewListener: IRecyclerViewListener? = null
    private var mLocationSharedPreferences: LocationSharedPreferences? = null
    private var mOfflineDataSharedPreference: OfflineDataSharedPreference? = null
    private var mCityName: TextView? = null
    private val mCardModelArrayList = ArrayList<CardModel>()
    private var mVolleyRequest: VolleyRequest? = null
    private var mIVolleyResponseCallback: IVolleyResponse? = null
    private var mLocationRecyclerViewAdapter: LocationRecyclerViewAdapter? = null
    private var CURRENTLOCATIONCITYID: String? = null
    private val CURRENTLOCATIONDEFAULTCITYID = "001"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialization()
        setDateTime()
    }

    /**
     * initialize the variables.
     */
    private fun initialization() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())
        JodaTimeAndroid.init(this)
        mCityName = findViewById(R.id.main_city_name)
        initSharedPref()
        initVolleyCallback()
        mVolleyRequest = VolleyRequest(this, mIVolleyResponseCallback)
        add_new_loc_btn.visibility = View.VISIBLE
        add_new_loc_btn.setOnClickListener(View.OnClickListener { searchForNewLocation() })
        if (!setCurrentCoordinates()) {
            fetchAllData(mLocationSharedPreferences!!.allLocations)
        }
        initRecyclerView()
        initPullToRefresh()
        if (!mIsInternetAvailable) {
            mCardModelArrayList.clear()
            mLocationRecyclerViewAdapter!!.clear()
            val cardModels = mOfflineDataSharedPreference!!.offlineData
            if (cardModels.size > 0) {
                mCardModelArrayList.addAll(cardModels)
            }
        }
    }

    private fun setDisconnectStatusBar(status: Boolean) {
        val networkStatus = findViewById<TextView>(R.id.network_status)
        if (!status) {
            networkStatus.visibility = View.VISIBLE
        } else {
            networkStatus.visibility = View.INVISIBLE
        }
    }

    private fun initPullToRefresh() {
        pullToRefresh.setOnRefreshListener(OnRefreshListener {
            if (!setCurrentCoordinates()) {
                fetchAllData(mLocationSharedPreferences!!.allLocations)
            }
            pullToRefresh.isRefreshing = true
        })
    }

    /**
     * Search engine from cursor.
     */
    private fun searchForNewLocation() {
        val customSearchDialog = CustomSearchDialog(this, this, ArrayList())
        customSearchDialog.show()

        customSearchDialog.setOnItemSelected(object : OnSearchItemSelection {
            override fun onClick(cityId: String?) {
                if (mLocationSharedPreferences!!.addNewLocation(cityId)) mVolleyRequest!!.getWeatherByCityId(cityId!!, WEATHER_BY_ID_HTTP_REQUEST) else Toast.makeText(this@MainActivity, "Already Exist", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Method to initialize the sharedPreference.
     */
    private fun initSharedPref() {
        mLocationSharedPreferences = LocationSharedPreferences(this)
        mOfflineDataSharedPreference = OfflineDataSharedPreference(this)
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
        cardModel.countryCode = mainResponse.sys!!.country
        cardModel.position = mLocationSharedPreferences!!.getPositionByCityId(java.lang.String.valueOf(mainResponse.id))!!.toInt()
        cardModel.temperature = java.lang.String.valueOf(mainResponse.main!!.temp)
        cardModel.time = mainResponse.dt.toLong()
        cardModel.secondsShift = mainResponse.timezone
        cardModel.weatherItem = mainResponse.weather!![0]
        cardModel.description = mainResponse.weather!![0].description!!.toUpperCase()
        cardModel.cityId = java.lang.String.valueOf(mainResponse.id)
        cardModel.dayNight = getDayNight(mainResponse)
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
        cardModel.cityId = CURRENTLOCATIONDEFAULTCITYID
        CURRENTLOCATIONCITYID = java.lang.String.valueOf(mainResponse.id)
        cardModel.temperature = java.lang.String.valueOf(mainResponse.main!!.temp)
        cardModel.weatherItem = mainResponse.weather!![0]
        cardModel.dayNight = getDayNight(mainResponse)
        cardModel.description = mainResponse.weather!![0].description!!.toUpperCase()
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
        for ((_, value) in allLocations) {
            mVolleyRequest!!.getWeatherByCityId(value.toString(), WEATHER_BY_ID_HTTP_REQUEST)
        }
        Handler().postDelayed({ pullToRefresh.isRefreshing = false }, 2000)
    }

    override fun onPause() {
        super.onPause()
        if (mCardModelArrayList.size > 0) {
            mOfflineDataSharedPreference!!.storeData(mCardModelArrayList)
        }
        mIsInternetAvailable = mOfflineDataSharedPreference!!.internetStatus
        Log.d(TAG, "onPause: $mIsInternetAvailable")
        if (mIsInternetAvailable) {
            modifyFeatures(true)
        } else {
            modifyFeatures(false)
            mCardModelArrayList.clear()
            mLocationRecyclerViewAdapter!!.clear()
            val cardModels = mOfflineDataSharedPreference!!.offlineData
            if (cardModels.size > 0) {
                mCardModelArrayList.addAll(cardModels)
            }
        }
        Log.d(TAG, "onPause: " + mCardModelArrayList.toTypedArray().contentToString())
    }

    private fun modifyFeatures(value: Boolean) {
        mOfflineDataSharedPreference!!.internetStatus = value
        pullToRefresh.isEnabled = value
        disableAddButton(value)
        setDisconnectStatusBar(value)
    }

    private fun disableAddButton(value: Boolean) {
        if (value) {
            add_new_loc_btn!!.visibility = View.VISIBLE
        } else {
            add_new_loc_btn!!.visibility = View.INVISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
    }

    /**
     * Method to set date/time continuously.
     */
    private fun setDateTime() {
        val someHandler = Handler(mainLooper)
        someHandler.postDelayed(object : Runnable {
            override fun run() {
                val calendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("EEEE, dd MMM", Locale.ENGLISH)
                val timeFormat = SimpleDateFormat("hh:mm aaa", Locale.ENGLISH)
                main_date.text = dateFormat.format(calendar.time)
                main_time.text = timeFormat.format(calendar.time)
                someHandler.postDelayed(this, 10000)
            }
        }, 10)
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
                mVolleyRequest!!.getWeatherByCoords(CURRENT_LOCATION_HTTP_REQUEST, location!!.longitude, location.latitude)
            }

            @SuppressLint("SetTextI18n")
            override fun onLocationNotFound() {
                mCityName!!.text = "Location not found!"
                fetchAllData(mLocationSharedPreferences!!.allLocations)
                Log.e(TAG, "onLocationNotFound: ")
            }
        })
        return retValue[0]
    }

    /**
     * Method for initializing the volleycalls.
     */
    private fun initVolleyCallback() {
        mIVolleyResponseCallback = object : IVolleyResponse {
            override fun onSuccessResponse(jsonObject: JsonObject?, requestType: String?) {
                when (requestType) {
                    CURRENT_LOCATION_HTTP_REQUEST -> {
                        addCurrentLocationData(jsonObject)
                        fetchAllData(mLocationSharedPreferences!!.allLocations)
                    }
                    WEATHER_BY_ID_HTTP_REQUEST -> addFavouriteCityWeather(jsonObject)
                }
            }

            override fun onRequestFailure(volleyError: VolleyError?, requestType: String?) {}
            override fun onSuccessJsonArrayResponse(jsonObject: JSONArray?, requestType: String?) {}
            override fun onStringSuccessRequest(response: String?, requestType: String?) {}
        }
    }

    /**
     * initialize the recycler view.
     */
    private fun initRecyclerView() {
        recyclerViewListener = object : IRecyclerViewListener {
            override fun onSingleShortClickListener(bundle: Bundle?) {
                openDetailedView(bundle)
            }
        }
        mLocationRecyclerViewAdapter = LocationRecyclerViewAdapter(mCardModelArrayList, this, recyclerViewListener)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        locations.layoutManager = layoutManager
        locations.adapter = mLocationRecyclerViewAdapter
        val touchHelper = RecyclerViewItemHelper<CardModel>(mCardModelArrayList, mLocationRecyclerViewAdapter as RecyclerView.Adapter<*>?)
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
                    Log.d(TAG, "onSwipeItemListener: remove location: " + mLocationSharedPreferences!!.removeLocation(myViewHolder.cityId))
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(touchHelper)
        itemTouchHelper.attachToRecyclerView(locations)
    }

    private fun openDetailedView(bundle: Bundle?) {
        val intent = Intent(this, WeatherDetail::class.java)
        if (bundle!!.getString(Constants.BUNDLE_CITY_ID) == CURRENTLOCATIONDEFAULTCITYID) {
            bundle.putString(Constants.BUNDLE_CITY_ID, CURRENTLOCATIONCITYID)
        }
        bundle.putBoolean(Constants.BUNDLE_INTERNET_AVAILABILITY, mIsInternetAvailable)
        intent.putExtra(Constants.BUNDLE_NAME, bundle)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    companion object {
        private const val TAG = "MainActivity"
        private var mIsInternetAvailable = false
    }
}