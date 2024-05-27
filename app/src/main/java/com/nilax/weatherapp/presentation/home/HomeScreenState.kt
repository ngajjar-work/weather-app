package com.nilax.weatherapp.presentation.home

import com.nilax.weatherapp.domain.model.WeeklyForecastData
import com.nilax.weatherapp.presentation.common.UiText

data class HomeScreenState(
    val isLoading: Boolean = false,
    val isValidSearch: Boolean = false,
    val searchedText: String = "",
    val weatherInfo: WeeklyForecastData? = null,
    val needRetryScreen: Boolean = true,
    val error: UiText? = null
)