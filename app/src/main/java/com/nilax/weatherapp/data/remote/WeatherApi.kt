package com.nilax.weatherapp.data.remote

import com.nilax.weatherapp.data.remote.dto.WeeklyWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("forecast")
    suspend fun getWeeklyForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") currentParameter: String,
        @Query("daily") dailyParameter: String
    ): Response<WeeklyWeatherResponse>
}
