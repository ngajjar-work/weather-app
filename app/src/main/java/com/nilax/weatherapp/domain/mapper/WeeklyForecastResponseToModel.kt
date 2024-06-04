package com.nilax.weatherapp.domain.mapper

import com.nilax.weatherapp.R
import com.nilax.weatherapp.common.Constants
import com.nilax.weatherapp.common.Mapper
import com.nilax.weatherapp.data.remote.dto.WeeklyWeatherResponse
import com.nilax.weatherapp.domain.model.WeatherIconSize
import com.nilax.weatherapp.domain.model.WeeklyForecast
import com.nilax.weatherapp.domain.model.WeeklyForecastData
import com.nilax.weatherapp.domain.usecase.GetWeatherInfoFromCodeUseCase
import com.nilax.weatherapp.presentation.common.UiText
import java.text.ParseException
import java.util.Date
import javax.inject.Inject

class WeeklyForecastResponseToModel @Inject constructor(
    private val currentDate: Date,
    private val weatherCodeUseCase: GetWeatherInfoFromCodeUseCase
) :
    Mapper<WeeklyWeatherResponse?, WeeklyForecastData> {

    override fun mapFrom(
        from: WeeklyWeatherResponse?
    ): WeeklyForecastData {

        val todayDateTime = Constants.sdfServerDate.format(currentDate)
        val list = mutableListOf<WeeklyForecast>()
        val isDay = from?.current?.isDay == 1
        var todayHigh = 0
        var todayLow = 0
        val weatherCode =
            weatherCodeUseCase(from?.current?.weatherCode ?: 0, WeatherIconSize.LARGE, isDay)


        from?.daily?.let {
            val listTime = it.time ?: emptyList()
            val listTempMax = it.temperature2mMax ?: emptyList()
            val listTempMin = it.temperature2mMin ?: emptyList()
            val listWeatherCode = it.weatherCode ?: emptyList()
            val minSize = minOf(
                listTime.lastIndex,
                listTempMax.lastIndex,
                listTempMin.lastIndex,
                listWeatherCode.lastIndex
            )

            for (index in 0..minSize) {
                val minTemp = Math.round(listTempMin[index])
                val maxTemp = Math.round(listTempMax[index])
                val dayInfo = if (todayDateTime == listTime[index]) {
                    todayLow = minTemp
                    todayHigh = maxTemp
                    UiText.StringResource(R.string.lbl_today)
                } else {
                    getDayInfo(listTime[index])
                }

                list.add(
                    WeeklyForecast(
                        dayInfo = dayInfo,
                        date = listTime[index],
                        high = maxTemp,
                        low = minTemp,
                        weatherCode = weatherCodeUseCase(
                            listWeatherCode[index],
                            WeatherIconSize.MEDIUM,
                            isDay
                        )
                    )
                )
            }
        }
        return WeeklyForecastData(
            Math.round(from?.current?.temperature2m ?: 0.0f),
            Math.round(from?.current?.apparentTemperature ?: 0.0f),
            todayHigh,
            todayLow,
            weatherCode,
            isDay,
            list
        )
    }

    private fun getDayInfo(dateString: String): UiText {
        return try {
            val parsedDate = Constants.sdfServerDate.parse(dateString)
            UiText.DynamicString(Constants.sdfDisplayDay.format(parsedDate))
        } catch (e: ParseException) {
            e.printStackTrace()
            UiText.DynamicString("")
        }
    }
}