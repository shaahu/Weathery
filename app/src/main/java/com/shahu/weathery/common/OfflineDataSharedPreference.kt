package com.shahu.weathery.common

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.shahu.weathery.model.CardModel
import java.util.*

/**
 * Created by Shahu Ronghe on 12, March, 2020
 * in Weathery
 */
class OfflineDataSharedPreference(private val mContext: Context?) {
    private val mOfflineDataKey = "offline_data_key"
    private val mInternetAvailability = "internet_avlb_key"
    private var mSharedPreferences: SharedPreferences? = null
    private fun init() {
        if (mContext != null) {
            mSharedPreferences = mContext.getSharedPreferences(Constants.OFFLINE_DATA_SHARED_PREFERENCE_NAME, 0)
        }
    }

    fun storeData(cardModelArrayList: ArrayList<CardModel>) {
        val gsonBuilder = GsonBuilder()
        val gson = gsonBuilder.create()
        val data = gson.toJson(cardModelArrayList)
        if (mSharedPreferences!!.contains(mOfflineDataKey)) {
            mSharedPreferences!!.edit().remove(mOfflineDataKey).apply()
        }
        mSharedPreferences!!.edit().putString(mOfflineDataKey, data).apply()
    }

    val offlineData: ArrayList<CardModel>
        get() {
            val data = mSharedPreferences!!.getString(mOfflineDataKey, null)
            if (data != null) {
                val gsonBuilder = GsonBuilder()
                val gson = gsonBuilder.create()
                val list = gson.fromJson(data, Array<CardModel>::class.java)
                return ArrayList(listOf(*list))
            }
            return ArrayList()
        }

    var internetStatus: Boolean
        get() = mSharedPreferences!!.getBoolean(mInternetAvailability, false)
        set(value) {
            mSharedPreferences!!.edit().putBoolean(mInternetAvailability, value).apply()
        }

    companion object {
        private const val TAG = "OfflineDataSharedPreference"
    }

    init {
        init()
    }
}