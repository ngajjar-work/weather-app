package com.nilax.weatherapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeeklyWeatherResponse(

    @field:SerializedName("current")
    val current: Current? = null,

    @field:SerializedName("current_units")
    val currentUnits: CurrentUnits? = null,

    @field:SerializedName("timezone")
    val timezone: String? = null,

    @field:SerializedName("latitude")
    val latitude: Any? = null,

    @field:SerializedName("daily")
    val daily: Daily? = null,

    @field:SerializedName("utc_offset_seconds")
    val utcOffsetSeconds: Int? = null,

    @field:SerializedName("longitude")
    val longitude: Any? = null
)

data class Daily(

    @field:SerializedName("temperature_2m_max")
    val temperature2mMax: List<Float>? = null,

    @field:SerializedName("temperature_2m_min")
    val temperature2mMin: List<Float>? = null,

    @field:SerializedName("time")
    val time: List<String>? = null
)

data class Current(

    @field:SerializedName("temperature_2m")
    val temperature2m: Float? = null,

    @field:SerializedName("interval")
    val interval: Int? = null,

    @field:SerializedName("time")
    val time: String? = null
)

data class CurrentUnits(

    @field:SerializedName("temperature_2m")
    val temperature2m: String? = null,

    @field:SerializedName("interval")
    val interval: String? = null,

    @field:SerializedName("time")
    val time: String? = null
)
