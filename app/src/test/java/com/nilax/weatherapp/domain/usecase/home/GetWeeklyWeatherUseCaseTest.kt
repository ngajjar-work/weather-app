package com.nilax.weatherapp.domain.usecase.home

import com.nhaarman.mockitokotlin2.whenever
import com.nilax.weatherapp.common.NetworkConstants
import com.nilax.weatherapp.common.network.Result
import com.nilax.weatherapp.data.remote.dto.WeeklyWeatherResponse
import com.nilax.weatherapp.domain.mapper.WeeklyForecastResponseToModel
import com.nilax.weatherapp.domain.model.WeeklyForecast
import com.nilax.weatherapp.domain.model.WeeklyForecastData
import com.nilax.weatherapp.domain.model.error.DataError
import com.nilax.weatherapp.domain.model.error.RootError
import com.nilax.weatherapp.domain.repo.WeatherRepository
import com.nilax.weatherapp.presentation.common.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response

class GetWeeklyWeatherUseCaseTest {

    @Mock
    private lateinit var repository: WeatherRepository

    @Mock
    private lateinit var mapper: WeeklyForecastResponseToModel

    private lateinit var sut: GetWeeklyWeatherUseCase

    private val latitude = NetworkConstants.DEFAULT_LAT_LNG.first
    private val longitude = NetworkConstants.DEFAULT_LAT_LNG.second
    private val currentParameter = NetworkConstants.CURRENT_PARAMS
    private val dailyParameter = NetworkConstants.DAILY_PARAMS

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sut = GetWeeklyWeatherUseCase(repository, mapper)
    }


    @Test
    fun `should return success when location is valid`() = runTest {
        // Given
        val mockResponse = mock(WeeklyWeatherResponse::class.java)
        val expectedSuccessData = WeeklyForecastData(
            currentTemp = 10,
            dailyHigh = 22,
            dailyLow = 8,
            weeklyForecast = emptyList()
        )

        whenever(
            repository.getWeeklyForecast(
                latitude,
                longitude,
                currentParameter,
                dailyParameter
            )
        )
            .thenReturn(Response.success(mockResponse))

        whenever(mapper.mapFrom(mockResponse)).thenReturn(expectedSuccessData)

        // When
        val resultFlow: Flow<Result<WeeklyForecastData, RootError>> = sut(
            latitude,
            longitude,
            currentParameter,
            dailyParameter
        )

        // Assert
        resultFlow.collect { result ->
            assertTrue(result is Result.Success)
            assertEquals(expectedSuccessData, (result as Result.Success).data)
        }
    }

    @Test
    fun `should return dummy weekly forecast list`() = runTest {
        // Given
        val dummyForecasts = listOf(
            WeeklyForecast("2024-05-27", UiText.DynamicString("Monday"), 30, 20),
            WeeklyForecast("2024-05-28", UiText.DynamicString("Tuesday"), 32, 22),
            WeeklyForecast("2024-05-29", UiText.DynamicString("Wednesday"), 28, 18)
        )

        val expectedSuccessData = WeeklyForecastData(
            currentTemp = 10,
            dailyHigh = 22,
            dailyLow = 8,
            weeklyForecast = dummyForecasts
        )

        val mockResponse = mock(WeeklyWeatherResponse::class.java)
        whenever(
            repository.getWeeklyForecast(
                latitude,
                longitude,
                currentParameter,
                dailyParameter
            )
        )
            .thenReturn(Response.success(mockResponse))

        whenever(mapper.mapFrom(mockResponse)).thenReturn(expectedSuccessData)

        // When
        val result = sut(
            latitude,
            longitude,
            currentParameter,
            dailyParameter
        ).first()

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(dummyForecasts, (result as Result.Success).data.weeklyForecast)
    }

    @Test
    fun `should return error for invalid parameters`() = runTest {

        val errorJson = "{\"error\": true,\"reason\": \"Latitude must be in range of -90 to 90\"}"

        val mockErrorResponse: Response<WeeklyWeatherResponse> = Response.error(
            400,
            errorJson.toResponseBody("application/json".toMediaTypeOrNull())
        )

        whenever(
            repository.getWeeklyForecast(
                latitude,
                longitude,
                currentParameter,
                dailyParameter
            )
        ).thenReturn(mockErrorResponse)

        // When
        val resultFlow: Flow<Result<WeeklyForecastData, RootError>> = sut(
            latitude,
            longitude,
            currentParameter,
            dailyParameter
        )

        // Assert
        resultFlow.collect { result ->
            assertTrue(result is Result.Error)
            assertTrue((result as Result.Error).error is DataError.CustomError)
            assertEquals(
                "Latitude must be in range of -90 to 90",
                (result.error as DataError.CustomError).message
            )
        }
    }


    @Test
    fun `should return UNKNOWN error for success response with null body`() = runTest {

        //arrange
        whenever(
            repository.getWeeklyForecast(
                latitude,
                longitude,
                currentParameter,
                dailyParameter
            )
        ).thenReturn(Response.success(null))

        //Act
        val resultFlow: Flow<Result<WeeklyForecastData, RootError>> = sut(
            latitude,
            longitude,
            currentParameter,
            dailyParameter
        )

        // Assert
        resultFlow.collect { result ->
            assertTrue(result is Result.Error)
            assertEquals(DataError.Network.UNKNOWN, (result as Result.Error).error)
        }

    }

    @Test
    fun `should return REQUEST_TIMEOUT error for code 408`() = runTest {

        val errorResponse = Response.error<Any>(408, "".toResponseBody())

        //arrange
        whenever(
            repository.getWeeklyForecast(
                latitude,
                longitude,
                currentParameter,
                dailyParameter
            )
        )
            .thenThrow(HttpException(errorResponse))

        //Act
        val resultFlow: Flow<Result<WeeklyForecastData, RootError>> = sut(
            latitude,
            longitude,
            currentParameter,
            dailyParameter
        )

        // Assert
        resultFlow.collect { result ->
            assertTrue(result is Result.Error)
            assertEquals(DataError.Network.REQUEST_TIMEOUT, (result as Result.Error).error)
        }
    }

    @Test
    fun `should return NO_INTERNET for IOException`() = runTest {

        // Arrange
        val expectedError = DataError.Network.NO_INTERNET
        whenever(
            repository.getWeeklyForecast(
                latitude,
                longitude,
                currentParameter,
                dailyParameter
            )
        ).thenAnswer {
            throw IOException()
        }

        // Act
        val resultFlow: Flow<Result<WeeklyForecastData, RootError>> = sut(
            latitude,
            longitude,
            currentParameter,
            dailyParameter
        )

        // Assert
        resultFlow.collect { result ->
            assertTrue(result is Result.Error)
            assertEquals(expectedError, (result as Result.Error).error)
        }
    }
}
