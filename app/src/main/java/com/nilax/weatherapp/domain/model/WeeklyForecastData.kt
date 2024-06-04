package com.nilax.weatherapp.domain.model

import com.nilax.weatherapp.presentation.common.UiText

data class WeeklyForecastData(
    val currentTemp: Int,
    val currentFeelsLike: Int,
    val dailyHigh: Int,
    val dailyLow: Int,
    val weatherCode: WeatherCodeInfo,
    val isDay: Boolean,
    val weeklyForecast: List<WeeklyForecast>,
)

data class WeeklyForecast(
    val date: String,
    val dayInfo: UiText,
    val high: Int,
    val low: Int,
    val weatherCode: WeatherCodeInfo,
)