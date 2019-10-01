package com.shahu.weathery.Common;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

import static com.shahu.weathery.Common.Constants.LOCATION_SHARED_PREFERENCE_NAME;

/**
 * Created by Shahu Ronghe on 19, September, 2019
 * in Weathery
 */
public class LocationSharedPreferences {
    private static final String TAG = "LocationSharedPreferenc";
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    public LocationSharedPreferences(Context context) {
        mContext = context;
        init();
    }

    private void init() {
        if (mContext != null) {
            mSharedPreferences = mContext.getSharedPreferences(LOCATION_SHARED_PREFERENCE_NAME, 0);
        }
    }

    public boolean addNewLocation(String cityId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (mSharedPreferences.getAll().containsValue(cityId))
            return false;
        editor.putString(getAvailablePosition(), cityId);
        editor.apply();
        return true;
    }

    private String getAvailablePosition() {
        int count = 1;
        for (String i : mSharedPreferences.getAll().keySet()) {
            if (i.equalsIgnoreCase(String.valueOf(count)))
                count++;
        }
        return String.valueOf(count);
    }

    public Map<String, ?> getAllLocations() {
        return mSharedPreferences.getAll();
    }

    public SharedPreferences getSharedPreferencesInstance() {
        return mSharedPreferences;
    }

    public String getPositionByCityId(int id) {
        String value = String.valueOf(id);
        for (Map.Entry<String, ?> entry : mSharedPreferences.getAll().entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public boolean removeLocation(String cityId) {
        if (!mSharedPreferences.contains(cityId)) {
            return false;
        }
        mSharedPreferences.edit().remove(cityId).apply();
        return true;
    }
}
