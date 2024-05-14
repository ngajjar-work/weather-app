package com.nilax.weatherapp.data.repo

import com.nilax.weatherapp.data.remote.WeatherApi
import com.nilax.weatherapp.data.remote.dto.WeeklyWeatherResponse
import com.nilax.weatherapp.domain.repo.WeatherRepository
import retrofit2.Response
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : WeatherRepository {


    override suspend fun getWeeklyForecast(
        latitude: Double,
        longitude: Double,
        currentParameter: String,
        dailyParameter: String
    ): Response<WeeklyWeatherResponse> {
        return weatherApi.getWeeklyForecast(latitude, longitude, currentParameter, dailyParameter)
    }

}