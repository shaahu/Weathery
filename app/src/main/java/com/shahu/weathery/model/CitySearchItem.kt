package com.shahu.weathery.model

import com.shahu.weathery.interface2.ISearchable

/**
 * Created by Shahu Ronghe on 26, November, 2019
 * in Weathery
 */
class CitySearchItem(var id: Int, override var cityName: String, var countryCode: String) : ISearchable {

    override fun toString(): String {
        return "CitySearchItem{" +
                "id=" + id +
                ", mCityName='" + cityName + '\'' +
                ", mCountryCode='" + countryCode + '\'' +
                '}'
    }

}