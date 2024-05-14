package com.nilax.weatherapp.common.network

import com.nilax.weatherapp.domain.model.error.RootError

sealed interface Result<out D, out E : RootError> {
    data class Success<out D, out E : RootError>(val data: D) :
        Result<D, E>

    data class Error<out D, out E : RootError>(val error: E) :
        Result<D, E>
}