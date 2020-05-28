package com.shahu.weathery.common

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.shahu.weathery.common.Constants.LOCATION_SHARED_PREFERENCE_NAME
import java.util.*

/**
 * Created by Shahu Ronghe on 19, September, 2019
 * in Weathery
 */
class LocationSharedPreferences(private val mContext: Context?) {
    var sharedPreferencesInstance: SharedPreferences? = null
        private set

    private fun init() {
        if (mContext != null) {
            sharedPreferencesInstance = mContext.getSharedPreferences(LOCATION_SHARED_PREFERENCE_NAME, 0)
        }
    }

    fun addNewLocation(cityId: String?): Boolean {
        val editor = sharedPreferencesInstance!!.edit()
        if (sharedPreferencesInstance!!.all.containsValue(cityId)) return false
        editor.putString(availablePosition, cityId)
        editor.apply()
        return true
    }

    private val availablePosition: String
        get() {
            var count = 1
            for (i in sharedPreferencesInstance!!.all.keys) {
                if (i.equals(count.toString(), ignoreCase = true)) count++
            }
            return count.toString()
        }

    val allLocations: Map<String, *>
        get() = sharedPreferencesInstance!!.all

    fun getPositionByCityId(id: String): String? {
        for ((key, value) in sharedPreferencesInstance!!.all) {
            if (id == value) {
                return key
            }
        }
        return null
    }

    fun removeLocation(cityId: String): Boolean {
        val key = getPositionByCityId(cityId)
        if (!sharedPreferencesInstance!!.contains(key)) {
            return false
        }
        sharedPreferencesInstance!!.edit().remove(key).apply()
        improveKeys()
        return true
    }

    private fun improveKeys() {
        var count = 1
        val sortedValues: MutableMap<Int, String> = TreeMap(Comparator { o1, o2 -> o1.compareTo(o2) })
        Log.d(TAG, "improveKeys: OldKeys: " + sharedPreferencesInstance!!.all.keys.toString())
        for ((key, value) in sharedPreferencesInstance!!.all) {
            sortedValues[Integer.valueOf(key)] = value.toString()
        }
        sharedPreferencesInstance!!.edit().clear().apply()
        val editor = sharedPreferencesInstance!!.edit()
        for ((_, value) in sortedValues) {
            editor.putString(count.toString(), value).apply()
            count++
        }
        Log.d(TAG, "improveKeys: NewKeys: " + sharedPreferencesInstance!!.all.keys.toString())
    }

    private fun getValueByPosition(pos: Int): String {
        return sharedPreferencesInstance!!.getString(pos.toString(), null)
    }

    private fun removeKey(pos: Int) {
        sharedPreferencesInstance!!.edit().remove(pos.toString()).apply()
    }

    private fun addKeyValues(pos: Int, value: String) {
        sharedPreferencesInstance!!.edit().putString(pos.toString(), value).apply()
    }

    fun updatePosition(fromPosition: Int, toPosition: Int) {
        val fromValue = getValueByPosition(fromPosition)
        val toValues = getValueByPosition(toPosition)
        removeKey(fromPosition)
        removeKey(toPosition)
        addKeyValues(toPosition, fromValue)
        addKeyValues(fromPosition, toValues)
        Log.d(TAG, "updatePosition: " + sharedPreferencesInstance!!.all)
    }

    companion object {
        private const val TAG = "LocationSharedPreferenc"
    }

    init {
        init()
    }
}