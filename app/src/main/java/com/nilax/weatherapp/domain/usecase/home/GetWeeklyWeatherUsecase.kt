package com.nilax.weatherapp.domain.usecase.home

import com.nilax.weatherapp.common.network.Result
import com.nilax.weatherapp.common.network.safeApiCall
import com.nilax.weatherapp.domain.mapper.WeeklyForecastResponseToModel
import com.nilax.weatherapp.domain.model.WeeklyForecastData
import com.nilax.weatherapp.domain.model.error.Error
import com.nilax.weatherapp.domain.repo.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetWeeklyWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository,
    private val mapper: WeeklyForecastResponseToModel
) {
    operator fun invoke(
        latitude: Double,
        longitude: Double,
        currentParameter: String,
        dailyParameter: String
    ): Flow<Result<WeeklyForecastData, Error>> = flow {
        val result = safeApiCall {
            repository.getWeeklyForecast(
                latitude,
                longitude,
                currentParameter,
                dailyParameter
            )
        }
        when (result) {
            is Result.Success -> emit(Result.Success(mapper.mapFrom(result.data)))
            is Result.Error -> emit(Result.Error(result.error))
        }
    }
}
