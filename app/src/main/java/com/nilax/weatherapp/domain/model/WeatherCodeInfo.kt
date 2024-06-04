package com.nilax.weatherapp.domain.model

data class WeatherCodeInfo(
    val code: Int,
    val description: String,
    val url: String
)

enum class WeatherIconSize {
    MEDIUM,
    LARGE
}