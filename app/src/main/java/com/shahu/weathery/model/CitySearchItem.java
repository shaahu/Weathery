package com.shahu.weathery.model;

import com.shahu.weathery.interface2.ISearchable;

/**
 * Created by Shahu Ronghe on 26, November, 2019
 * in Weathery
 */
public class CitySearchItem implements ISearchable {
    private int id;
    private String mCityName;
    private String mCountryCode;

    public CitySearchItem(int id, String cityName, String mCountryCode) {
        this.id = id;
        this.mCityName = cityName;
        this.mCountryCode = mCountryCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String countryCode) {
        this.mCountryCode = countryCode;
    }

    @Override
    public String toString() {
        return "CitySearchItem{" +
                "id=" + id +
                ", mCityName='" + mCityName + '\'' +
                ", mCountryCode='" + mCountryCode + '\'' +
                '}';
    }

    @Override
    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        this.mCityName = cityName;
    }
}
