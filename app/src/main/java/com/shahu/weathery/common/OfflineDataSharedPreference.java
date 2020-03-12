package com.shahu.weathery.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shahu.weathery.model.CardModel;

import java.util.ArrayList;
import java.util.Arrays;

import static com.shahu.weathery.common.Constants.OFFLINE_DATA_SHARED_PREFERENCE_NAME;

/**
 * Created by Shahu Ronghe on 12, March, 2020
 * in Weathery
 */
public class OfflineDataSharedPreference {
    private static final String TAG = "OfflineDataSharedPreference";
    private final String mOfflineDataKey = "offline_data_key";
    private final String mInternetAvailability = "internet_avlb_key";
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    public OfflineDataSharedPreference(Context context) {
        mContext = context;
        init();
    }

    private void init() {
        if (mContext != null) {
            mSharedPreferences = mContext.getSharedPreferences(OFFLINE_DATA_SHARED_PREFERENCE_NAME, 0);
        }
    }


    public void storeData(ArrayList<CardModel> cardModelArrayList) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        final String data = gson.toJson(cardModelArrayList);
        if (mSharedPreferences.contains(mOfflineDataKey)) {
            mSharedPreferences.edit().remove(mOfflineDataKey).apply();
        }
        mSharedPreferences.edit().putString(mOfflineDataKey, data).apply();
    }

    public ArrayList<CardModel> getOfflineData() {
        String data = mSharedPreferences.getString(mOfflineDataKey, null);
        if (data != null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            CardModel[] list = gson.fromJson(data, CardModel[].class);
            return new ArrayList<>(Arrays.asList(list));
        }
        return new ArrayList<>();
    }

    public boolean getInternetStatus() {
        return mSharedPreferences.getBoolean(mInternetAvailability, false);
    }

    public void setInternetStatus(boolean value) {
        mSharedPreferences.edit().putBoolean(mInternetAvailability, value).apply();
    }
}
