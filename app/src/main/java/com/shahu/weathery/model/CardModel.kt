package com.shahu.weathery.model

import com.shahu.weathery.model.common.WeatherItem
import java.io.Serializable

/**
 * Created by Shahu Ronghe on 20, September, 2019
 * in Weathery
 */
class CardModel : Serializable {
    var name: String? = null
    var countryCode: String? = null
    var position = 0
    var weatherItem: WeatherItem? = null
    var temperature: String? = null
    var description: String? = null
    var cityId: String? = null
    var dayNight: String? = null
    var time: Long = 0
    var secondsShift = 0

    constructor(name: String?, countryCode: String?, position: Int, weatherItem: WeatherItem?, temperature: String?, description: String?, cityId: String?, dayNight: String?, time: Long, secondsShift: Int) {
        this.name = name
        this.countryCode = countryCode
        this.position = position
        this.weatherItem = weatherItem
        this.temperature = temperature
        this.description = description
        this.cityId = cityId
        this.dayNight = dayNight
        this.time = time
        this.secondsShift = secondsShift
    }

    constructor(cardModel: CardModel) {
        name = cardModel.name
        position = cardModel.position
        weatherItem = cardModel.weatherItem
        temperature = cardModel.temperature
        description = cardModel.description
        cityId = cardModel.cityId
    }

    constructor() {}

    override fun toString(): String {
        return "Position: " + position + ", Name: " + name + ", Temperature: " + temperature + ", Description:" +
                " " + description
    }
}