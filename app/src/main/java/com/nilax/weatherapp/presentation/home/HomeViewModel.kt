package com.nilax.weatherapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nilax.weatherapp.common.NetworkConstants
import com.nilax.weatherapp.common.network.Result
import com.nilax.weatherapp.domain.model.error.DataError
import com.nilax.weatherapp.domain.usecase.LocationValidator
import com.nilax.weatherapp.domain.usecase.home.GetWeeklyWeatherUseCase
import com.nilax.weatherapp.presentation.common.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: GetWeeklyWeatherUseCase,
    private val locationValidator: LocationValidator
) : ViewModel() {

    // state of searched latitude longitude
    private val _locationInfo = MutableStateFlow(NetworkConstants.DEFAULT_LAT_LNG)

    // state of weather api
    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState: StateFlow<HomeScreenState> get() = _homeScreenState

    init {
        fetchWeeklyData(_locationInfo.value.first, _locationInfo.value.second)
    }

    private fun fetchWeeklyData(latitude: Double, longitude: Double) {
        _homeScreenState.value = _homeScreenState.value.copy(isLoading = true, error = null)

        useCase.invoke(
            latitude,
            longitude,
            NetworkConstants.CURRENT_PARAMS,
            NetworkConstants.DAILY_PARAMS
        )
            .onEach { result ->
                _homeScreenState.value = when (result) {
                    is Result.Error -> {
                        _homeScreenState.value.copy(
                            canRetryOnError = _homeScreenState.value.weatherInfo != null,
                            isLoading = false,
                            weatherInfo = null,
                            error = (result.error as DataError).asUiText(),
                        )
                    }

                    is Result.Success -> {
                        _locationInfo.emit(Pair(latitude, longitude))
                        _homeScreenState.value.copy(
                            isLoading = false,
                            weatherInfo = result.data,
                            error = null,
                            canRetryOnError = true,
                            searchedText = "$latitude, $longitude"
                        )
                    }
                }

            }.launchIn(viewModelScope)
    }

    fun searchWeatherByCoordinates(coordinates: String) {
        if (locationValidator.isValidLocation(coordinates)) {
            val latLong = locationValidator.getLocationFromString(coordinates)
            fetchWeeklyData(latLong.first, latLong.second)
        }
    }

    fun onTextChanged(newText: String) {
        _homeScreenState.value =
            _homeScreenState.value.copy(isValidSearch = locationValidator.isValidLocation(newText))
    }

    fun onRetry() {
        fetchWeeklyData(_locationInfo.value.first, _locationInfo.value.second)
    }
}
