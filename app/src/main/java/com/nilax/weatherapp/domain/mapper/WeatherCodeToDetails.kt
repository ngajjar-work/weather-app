package com.nilax.weatherapp.domain.mapper

import com.nilax.weatherapp.common.Mapper
import javax.inject.Inject

class WeatherCodeToDetails @Inject constructor() : Mapper<Int, Pair<String, String>> {

    companion object {
        val weatherCodeMap: Map<Int, Pair<String, String>> = mapOf(
            0 to Pair("Clear Sky", "01"),
            1 to Pair("Mainly Clear", "01"),
            2 to Pair("Partly Cloudy", "02"),
            3 to Pair("Overcast", "03"),
            45 to Pair("Foggy", "50"),
            48 to Pair("Depositing Rime Fog", "50"),
            51 to Pair("Light Drizzle", "09"),
            53 to Pair("Moderate Drizzle", "09"),
            55 to Pair("Dense Drizzle", "09"),
            56 to Pair("Light Freezing Drizzle", "09"),
            57 to Pair("Freezing Drizzle", "09"),
            61 to Pair("Light Rain", "10"),
            63 to Pair("Moderate Rain", "10"),
            65 to Pair("Heavy Rain", "10"),
            66 to Pair("Light Freezing Rain", "10"),
            67 to Pair("Freezing Rain", "10"),
            71 to Pair("Light Snow", "13"),
            73 to Pair("Moderate Snow", "13"),
            75 to Pair("Heavy Snow", "13"),
            77 to Pair("Snow Grains", "13"),
            80 to Pair("Light Rain Showers", "09"),
            81 to Pair("Moderate Rain Showers", "09"),
            82 to Pair("Heavy Rain Showers", "09"),
            85 to Pair("Light Snow Showers", "13"),
            86 to Pair("Snow Showers", "13"),
            95 to Pair("Thunderstorm", "11"),
            96 to Pair("Thunderstorm with Light Hail", "11"),
            99 to Pair("Thunderstorm with Hail", "11")
        )
    }


    override fun mapFrom(from: Int): Pair<String, String> {
        return weatherCodeMap[from] ?: Pair("Clear Sky", "01")
    }
}