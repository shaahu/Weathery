package com.shahu.weathery.Common;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

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

    public boolean removeLocation(int cityId) {
        String key = getPositionByCityId(cityId);
        if (!mSharedPreferences.contains(key)) {
            return false;
        }
        mSharedPreferences.edit().remove(key).apply();
        improveKeys();
        return true;
    }

    private void improveKeys() {
        int count = 1;
        Map<Integer, String> sortedValues = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        Log.d(TAG, "improveKeys: OldKeys: " + mSharedPreferences.getAll().keySet().toString());
        for (Map.Entry<String, ?> entry : mSharedPreferences.getAll().entrySet()) {
            sortedValues.put(Integer.valueOf(entry.getKey()), entry.getValue().toString());
        }
        mSharedPreferences.edit().clear().apply();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (Map.Entry<Integer, String> entry : sortedValues.entrySet()) {
            editor.putString(String.valueOf(count), entry.getValue()).apply();
            count++;
        }
        Log.d(TAG, "improveKeys: NewKeys: " + mSharedPreferences.getAll().keySet().toString());
    }

    private String getValueByPosition(int pos) {
        return mSharedPreferences.getString(String.valueOf(pos), null);
    }

    private void removeKey(int pos) {
        mSharedPreferences.edit().remove(String.valueOf(pos)).apply();
    }

    private void addKeyValues(int pos, String value) {
        mSharedPreferences.edit().putString(String.valueOf(pos), value).apply();
    }

    public void updatePosition(int fromPosition, int toPosition) {
        String fromValue = getValueByPosition(fromPosition);
        String toValues = getValueByPosition(toPosition);
        removeKey(fromPosition);
        removeKey(toPosition);
        addKeyValues(toPosition, fromValue);
        addKeyValues(fromPosition, toValues);
        Log.d(TAG, "updatePosition: " + mSharedPreferences.getAll());
    }
}
