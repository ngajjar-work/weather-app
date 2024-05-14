package com.nilax.weatherapp.domain.mapper

import com.nilax.weatherapp.R
import com.nilax.weatherapp.common.Constants
import com.nilax.weatherapp.data.remote.dto.Current
import com.nilax.weatherapp.data.remote.dto.CurrentUnits
import com.nilax.weatherapp.data.remote.dto.Daily
import com.nilax.weatherapp.data.remote.dto.WeeklyWeatherResponse
import com.nilax.weatherapp.domain.model.WeeklyForecast
import com.nilax.weatherapp.domain.model.WeeklyForecastData
import com.nilax.weatherapp.presentation.common.UiText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.util.Date

class WeeklyForecastResponseToModelTest {

    private lateinit var sut: WeeklyForecastResponseToModel
    private lateinit var currentDate: Date

    @Before
    fun setUp() {
        currentDate = Constants.sdfServerDate.parse("2024-05-20") ?: Date()
        sut = WeeklyForecastResponseToModel(currentDate)
    }

    @Test
    fun `mapFrom should return empty data when response is null`() {
        val result = sut.mapFrom(null)
        assertNotNull(result)
        assertEquals(0, result.dailyLow)
        assertEquals(0, result.dailyHigh)
        assertEquals(0, result.currentTemp)
        assertEquals(0, result.weeklyForecast.size)
    }

    @Test
    fun `mapFrom should return expected data for valid response`() {
        val timeList = listOf("2024-05-20", "2024-05-21", "2024-05-22")

        val dailyTemp = Daily(
            temperature2mMax = listOf(20f, 22f, 24f),
            temperature2mMin = listOf(10f, 12f, 14f),
            time = timeList
        )

        val current = Current(
            temperature2m = 15f,
            time = Constants.sdfServerDateTime.format(currentDate),
            interval = 100
        )
        val currentUnits =
            CurrentUnits(time = "iso8601", interval = "seconds", temperature2m = "°C")

        val expectedWeeklyForecast = listOf(
            createWeeklyForecast(
                timeList[0],
                R.string.lbl_today,
                dailyTemp.temperature2mMax?.get(0) ?: 0f,
                dailyTemp.temperature2mMin?.get(0) ?: 0f
            ),
            createWeeklyForecast(
                timeList[1],
                "Tuesday",
                dailyTemp.temperature2mMax?.get(1) ?: 0f,
                dailyTemp.temperature2mMin?.get(1) ?: 0f
            ),
            createWeeklyForecast(
                timeList[2],
                "Wednesday",
                dailyTemp.temperature2mMax?.get(2) ?: 0f,
                dailyTemp.temperature2mMin?.get(2) ?: 0f
            )
        )

        val expectedResult = WeeklyForecastData(
            currentTemp = Math.round(current.temperature2m ?: 0f),
            dailyHigh = Math.round(dailyTemp.temperature2mMax?.first() ?: 0f),
            dailyLow = Math.round(dailyTemp.temperature2mMin?.first() ?: 0f),
            weeklyForecast = expectedWeeklyForecast
        )

        val response = WeeklyWeatherResponse(
            current = current,
            currentUnits = currentUnits,
            daily = dailyTemp
        )

        val result = sut.mapFrom(response)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `mapFrom should return data with minimal properties when response has minimal size`() {
        val timeList = listOf("2024-05-20", "2024-05-21", "2024-05-22")

        val dailyTemp = Daily(
            temperature2mMax = listOf(20f),
            temperature2mMin = listOf(10f, 12f, 14f),
            time = timeList
        )

        val current = Current(
            temperature2m = 15f,
            time = Constants.sdfServerDateTime.format(currentDate),
            interval = 100
        )
        val currentUnits =
            CurrentUnits(time = "iso8601", interval = "seconds", temperature2m = "°C")

        val expectedWeeklyForecast = listOf(
            createWeeklyForecast(
                timeList[0],
                R.string.lbl_today,
                dailyTemp.temperature2mMax?.get(0) ?: 0f,
                dailyTemp.temperature2mMin?.get(0) ?: 0f
            )
        )

        val expectedResult = WeeklyForecastData(
            currentTemp = Math.round(current.temperature2m ?: 0f),
            dailyHigh = Math.round(dailyTemp.temperature2mMax?.first() ?: 0f),
            dailyLow = Math.round(dailyTemp.temperature2mMin?.first() ?: 0f),
            weeklyForecast = expectedWeeklyForecast
        )

        val response = WeeklyWeatherResponse(
            current = current,
            currentUnits = currentUnits,
            daily = dailyTemp
        )

        val result = sut.mapFrom(response)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `mapFrom should return empty data for broken response`() {
        val timeList = listOf("2024-05-20", "2024-05-21", "2024-05-22")

        val dailyTemp = Daily(
            temperature2mMax = listOf(20f),
            time = timeList
        )
        val current = Current(
            temperature2m = 15f,
            time = Constants.sdfServerDateTime.format(currentDate),
            interval = 100
        )
        val currentUnits =
            CurrentUnits(time = "iso8601", interval = "seconds", temperature2m = "°C")

        val expectedResult = WeeklyForecastData(
            currentTemp = Math.round(current.temperature2m ?: 0f),
            dailyHigh = 0,
            dailyLow = 0,
            weeklyForecast = emptyList()
        )

        val response = WeeklyWeatherResponse(
            current = current,
            currentUnits = currentUnits,
            daily = dailyTemp
        )

        val result = sut.mapFrom(response)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `mapFrom should return empty string for invalid date format`() {
        val timeList = listOf("2024-May-22")

        val dailyTemp = Daily(
            temperature2mMax = listOf(20f),
            temperature2mMin = listOf(10f),
            time = timeList
        )

        val current = Current(
            temperature2m = 15f,
            time = Constants.sdfServerDateTime.format(currentDate),
            interval = 100
        )
        val currentUnits =
            CurrentUnits(time = "iso8601", interval = "seconds", temperature2m = "°C")

        val expectedWeeklyForecast = listOf(
            createWeeklyForecast(
                timeList[0],
                "",
                dailyTemp.temperature2mMax?.get(0) ?: 0f,
                dailyTemp.temperature2mMin?.get(0) ?: 0f
            )
        )

        val expectedResult = WeeklyForecastData(
            currentTemp = Math.round(current.temperature2m ?: 0f),
            dailyHigh = 0,
            dailyLow = 0,
            weeklyForecast = expectedWeeklyForecast
        )

        val response = WeeklyWeatherResponse(
            current = current,
            currentUnits = currentUnits,
            daily = dailyTemp
        )

        val result = sut.mapFrom(response)

        assertEquals(expectedResult, result)
    }

    private fun createWeeklyForecast(
        date: String,
        dayInfo: Any,
        high: Float,
        low: Float
    ): WeeklyForecast {
        val dayInfoUiText = if (dayInfo is Int) {
            UiText.StringResource(dayInfo)
        } else {
            UiText.DynamicString(dayInfo.toString())
        }
        return WeeklyForecast(
            date = date,
            dayInfo = dayInfoUiText,
            high = Math.round(high),
            low = Math.round(low)
        )
    }
}