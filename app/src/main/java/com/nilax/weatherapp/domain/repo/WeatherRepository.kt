package com.nilax.weatherapp.domain.repo

import com.nilax.weatherapp.data.remote.dto.WeeklyWeatherResponse
import retrofit2.Response

interface WeatherRepository {

    suspend fun getWeeklyForecast(
        latitude: Double,
        longitude: Double,
        currentParameter: String,
        dailyParameter: String
    ): Response<WeeklyWeatherResponse>
}