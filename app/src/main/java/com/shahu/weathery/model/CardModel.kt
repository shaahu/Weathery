package com.shahu.weathery.model

import android.os.Parcel
import android.os.Parcelable
import com.shahu.weathery.model.main.WeatherItem

/**
 * Created by Shahu Ronghe on 20, September, 2019
 * in Weathery
 */
class CardModel() : Parcelable {
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
    var lon: String? = null
    var lat: String? = null

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        countryCode = parcel.readString()
        position = parcel.readInt()
        temperature = parcel.readString()
        description = parcel.readString()
        cityId = parcel.readString()
        dayNight = parcel.readString()
        time = parcel.readLong()
        secondsShift = parcel.readInt()
        lon = parcel.readString()
        lat = parcel.readString()
    }


    override fun toString(): String {
        return "Position: " + position + ", Name: " + name + ", Temperature: " + temperature + ", Description:" +
                " " + description
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(countryCode)
        parcel.writeInt(position)
        parcel.writeString(temperature)
        parcel.writeString(description)
        parcel.writeString(cityId)
        parcel.writeString(dayNight)
        parcel.writeLong(time)
        parcel.writeInt(secondsShift)
        parcel.writeString(lon)
        parcel.writeString(lat)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CardModel> {
        override fun createFromParcel(parcel: Parcel): CardModel {
            return CardModel(parcel)
        }

        override fun newArray(size: Int): Array<CardModel?> {
            return arrayOfNulls(size)
        }
    }
}
