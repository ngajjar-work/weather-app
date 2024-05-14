package com.nilax.weatherapp.data.repo

import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.whenever
import com.nilax.weatherapp.common.NetworkConstants
import com.nilax.weatherapp.data.remote.WeatherApi
import com.nilax.weatherapp.data.remote.dto.APIError
import com.nilax.weatherapp.data.remote.dto.WeeklyWeatherResponse
import com.nilax.weatherapp.domain.repo.WeatherRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Response


class WeatherRepositoryImplTest {

    @Mock
    lateinit var weatherApi: WeatherApi
    private lateinit var weatherRepository: WeatherRepository

    private val expectedLatitude = NetworkConstants.DEFAULT_LAT_LNG.first
    private val expectedLongitude = NetworkConstants.DEFAULT_LAT_LNG.second
    private val expectedCurrentParameter = NetworkConstants.CURRENT_PARAMS
    private val expectedDailyParameter = NetworkConstants.DAILY_PARAMS


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        weatherRepository = WeatherRepositoryImpl(weatherApi)
    }

    @Test
    fun `getWeeklyForecast returns successful response`() = runTest {

        val mockResponse = Response.success(WeeklyWeatherResponse())
        whenever(
            weatherApi.getWeeklyForecast(
                expectedLatitude,
                expectedLongitude,
                expectedCurrentParameter,
                expectedDailyParameter
            )
        ).thenReturn(mockResponse)

        val actualResponse = weatherRepository.getWeeklyForecast(
            expectedLatitude,
            expectedLongitude,
            expectedCurrentParameter,
            expectedDailyParameter
        )

        assertEquals(mockResponse, actualResponse) // Assert the returned response is the same
    }

    @Test
    fun `getWeeklyForecast returns failure response`() = runTest {

        val expectedErrorCode = 400
        val expectedErrorReason = "Invalid parameters"
        val errorJson = Gson().toJson(APIError(reason = expectedErrorReason, error = true))

        val mockErrorResponse = Response.error<WeeklyWeatherResponse>(
            expectedErrorCode,
            errorJson.toResponseBody("application/json".toMediaTypeOrNull())
        )

        whenever(
            weatherApi.getWeeklyForecast(
                expectedLatitude,
                expectedLongitude,
                expectedCurrentParameter,
                expectedDailyParameter
            )
        ).thenReturn(mockErrorResponse)

        val actualResponse = weatherRepository.getWeeklyForecast(
            expectedLatitude,
            expectedLongitude,
            expectedCurrentParameter,
            expectedDailyParameter
        )

        val apiError: APIError = Gson().fromJson(
            actualResponse.errorBody()?.charStream(),
            APIError::class.java
        )


        assertEquals(expectedErrorCode, actualResponse.code())
        assertEquals(expectedErrorReason, apiError.reason)
        assertTrue(apiError.error)
    }
}