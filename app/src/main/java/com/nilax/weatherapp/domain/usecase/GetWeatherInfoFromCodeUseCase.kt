package com.nilax.weatherapp.domain.usecase

import com.nilax.weatherapp.common.NetworkConstants
import com.nilax.weatherapp.domain.mapper.WeatherCodeToDetails
import com.nilax.weatherapp.domain.model.WeatherCodeInfo
import com.nilax.weatherapp.domain.model.WeatherIconSize
import javax.inject.Inject

class GetWeatherInfoFromCodeUseCase @Inject constructor(
    private val mapper: WeatherCodeToDetails
) {

    operator fun invoke(
        code: Int,
        size: WeatherIconSize,
        isDayTime: Boolean = true
    ): WeatherCodeInfo {
        val weatherCodeDetails = mapper.mapFrom(code)
        val url = String.format(
            NetworkConstants.ICON_URL,
            weatherCodeDetails.second,
            getIconStyle(isDayTime),
            getSizeInfo(size)
        )
        return WeatherCodeInfo(code, weatherCodeDetails.first, url)
    }


    private fun getSizeInfo(weatherIconSize: WeatherIconSize): String {
        return when (weatherIconSize) {
            WeatherIconSize.MEDIUM -> {
                "2x"
            }

            WeatherIconSize.LARGE -> {
                "4x"
            }
        }
    }

    private fun getIconStyle(isDayTime: Boolean): String {
        return if (isDayTime) "d" else "n"
    }
}