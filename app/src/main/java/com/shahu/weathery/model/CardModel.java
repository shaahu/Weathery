package com.shahu.weathery.model;

import android.support.annotation.NonNull;

/**
 * Created by Shahu Ronghe on 20, September, 2019
 * in Weathery
 */
public class CardModel {
    private String mName;
    private String mCountryCode;
    private int mPosition;
    private WeatherItem mWeatherItem;
    private String mTemperature;
    private String mDescription;
    private int mCityId;
    private String mDayNight;

    public CardModel(String name, String countryCode, int position, WeatherItem weatherItem, String temperature, String description, int cityId, String dayNight) {
        mName = name;
        mCountryCode = countryCode;
        mPosition = position;
        mWeatherItem = weatherItem;
        mTemperature = temperature;
        mDescription = description;
        mCityId = cityId;
        mDayNight = dayNight;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String countryCode) {
        mCountryCode = countryCode;
    }

    public CardModel(CardModel cardModel) {
        this.mName = cardModel.mName;
        this.mPosition = cardModel.mPosition;
        this.mWeatherItem = cardModel.mWeatherItem;
        this.mTemperature = cardModel.mTemperature;
        this.mDescription = cardModel.mDescription;
        this.mCityId = cardModel.mCityId;
    }

    public CardModel() {
    }

    public String getDayNight() {
        return mDayNight;
    }

    public void setDayNight(String dayNight) {
        mDayNight = dayNight;
    }

    public int getCityId() {
        return mCityId;
    }

    public void setCityId(int cityId) {
        mCityId = cityId;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public WeatherItem getWeatherItem() {
        return mWeatherItem;
    }

    public void setWeatherItem(WeatherItem weatherItem) {
        mWeatherItem = weatherItem;
    }

    public String getTemperature() {
        return mTemperature;
    }

    public void setTemperature(String temperature) {
        mTemperature = temperature;
    }

    @NonNull
    @Override
    public String toString() {
        return ("Position: " + this.getPosition() + ", Name: " + this.getName() + ", Temperature: " + this.getTemperature() + ", Description:" +
                " " + this.getDescription());
    }
}
