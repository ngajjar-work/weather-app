package com.nilax.weatherapp.presentation.home

import com.nhaarman.mockitokotlin2.clearInvocations
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.nilax.weatherapp.base.BaseTest
import com.nilax.weatherapp.common.NetworkConstants
import com.nilax.weatherapp.common.network.Result
import com.nilax.weatherapp.domain.model.WeeklyForecastData
import com.nilax.weatherapp.domain.model.error.DataError
import com.nilax.weatherapp.domain.model.error.RootError
import com.nilax.weatherapp.domain.usecase.LocationValidator
import com.nilax.weatherapp.domain.usecase.home.GetWeeklyWeatherUseCase
import com.nilax.weatherapp.presentation.common.asUiText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.Mock

class HomeViewModelTest : BaseTest() {

    @Mock
    private lateinit var useCase: GetWeeklyWeatherUseCase

    @Mock
    private lateinit var locationValidator: LocationValidator

    private lateinit var sut: HomeViewModel

    private val expectedLatitude = NetworkConstants.DEFAULT_LAT_LNG.first
    private val expectedLongitude = NetworkConstants.DEFAULT_LAT_LNG.second
    private val expectedCurrentParameter = NetworkConstants.CURRENT_PARAMS
    private val expectedDailyParameter = NetworkConstants.DAILY_PARAMS

    private val expectedWeeklyForecastData = WeeklyForecastData(
        0,
        0,
        0,
        emptyList()
    )

    private val expectedError = DataError.Network.UNKNOWN.asUiText()


    @Test
    fun `should call use case when ViewModel is initialized`() = runTest {
        //Arrange
        sut = getSuccessCase()

        //Act
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        verify(useCase, times(1)).invoke(
            expectedLatitude,
            expectedLongitude,
            expectedCurrentParameter,
            expectedDailyParameter
        )
    }

    @Test
    fun `should update view state when use case returns success`() = runTest {

        //Arrange
        sut = getSuccessCase()

        //should showLoading
        Assert.assertTrue(sut.homeScreenState.value.isLoading)

        // Act
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert final state
        Assert.assertEquals(expectedWeeklyForecastData, sut.homeScreenState.value.weatherInfo)
        Assert.assertFalse(sut.homeScreenState.value.isLoading)
        Assert.assertNull(sut.homeScreenState.value.error)
        Assert.assertFalse(sut.homeScreenState.value.needRetryScreen)
    }

    @Test
    fun `should update view state when use case returns failure`() = runTest {
        //Arrange
        sut = getFailureCase()

        // Act
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert final state
        Assert.assertEquals(expectedError, sut.homeScreenState.value.error)
    }

    @Test
    fun `should search for location when location is valid`() = runTest {

        //Arrange
        sut = getSuccessCase()

        val validCoordinates = "12,12"
        val validLatLong = Pair(12.0, 12.0)

        whenever(locationValidator.isValidLocation(validCoordinates)).thenReturn(true)

        whenever(locationValidator.getLocationFromString(validCoordinates)).thenReturn(validLatLong)

        val successResponse: Flow<Result<WeeklyForecastData, RootError>> = flow {
            emit(Result.Success(expectedWeeklyForecastData))
        }

        whenever(
            useCase(
                validLatLong.first,
                validLatLong.second,
                expectedCurrentParameter,
                expectedDailyParameter
            )
        ).thenReturn(successResponse)

        // Act
        sut.searchWeatherByCoordinates(validCoordinates)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        verify(useCase, times(1)).invoke(
            expectedLatitude,
            expectedLongitude,
            expectedCurrentParameter,
            expectedDailyParameter
        )

        verify(useCase, times(1)).invoke(
            validLatLong.first,
            validLatLong.second,
            expectedCurrentParameter,
            expectedDailyParameter
        )
    }

    @Test
    fun `should return isValidSearch true when search text is in valid`() = runTest {
        //Arrange
        sut = getSuccessCase()

        val validCoordinates = "12,12"

        whenever(locationValidator.isValidLocation(validCoordinates)).thenReturn(true)

        sut.onTextChanged(validCoordinates)

        Assert.assertTrue(sut.homeScreenState.value.isValidSearch)

    }

    @Test
    fun `should return isValidSearch false when search text is invalid`() = runTest {
        //Arrange
        sut = getSuccessCase()

        val validCoordinates = "122"

        whenever(locationValidator.isValidLocation(validCoordinates)).thenReturn(false)

        sut.onTextChanged(validCoordinates)

        Assert.assertFalse(sut.homeScreenState.value.isValidSearch)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should call use case when retry is called`() = runTest {

        // Arrange
        sut = getFailureCase()

        // Clear the invocations to isolate the retry call
        clearInvocations(useCase)

        // Act
        sut.onRetry()

        // Assert - useCase called after onRetry
        verify(useCase, times(1)).invoke(
            expectedLatitude,
            expectedLongitude,
            expectedCurrentParameter,
            expectedDailyParameter
        )
    }


    private fun getSuccessCase(): HomeViewModel {
        val successResponse: Flow<Result<WeeklyForecastData, RootError>> =
            flowOf(Result.Success(expectedWeeklyForecastData))
        whenever(
            useCase(
                expectedLatitude,
                expectedLongitude,
                expectedCurrentParameter,
                expectedDailyParameter
            )
        ).thenReturn(successResponse)
        return HomeViewModel(useCase, locationValidator)
    }

    private fun getFailureCase(): HomeViewModel {
        val failureResponse: Flow<Result<WeeklyForecastData, RootError>> =
            flowOf(Result.Error(DataError.Network.UNKNOWN))
        whenever(
            useCase(
                expectedLatitude,
                expectedLongitude,
                expectedCurrentParameter,
                expectedDailyParameter
            )
        ).thenReturn(failureResponse)
        return HomeViewModel(useCase, locationValidator)
    }

}