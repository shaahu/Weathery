package com.shahu.weathery.model.citysearch

data class CitySearchResultResponseItem(
        val coord: Coord,
        val country: String,
        val id: Int,
        val name: String,
        val state: String
)