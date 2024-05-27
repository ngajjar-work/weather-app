package com.nilax.weatherapp.common.network

import com.nilax.weatherapp.data.remote.dto.APIError
import com.nilax.weatherapp.domain.model.error.DataError
import retrofit2.HttpException
import retrofit2.Response


fun Response<*>.mapError(): DataError {
    val errorBody: APIError? = errorBody()?.getErrorObject<APIError>()
    return when (code()) {
        408 -> DataError.Network.REQUEST_TIMEOUT
        413 -> DataError.Network.PAYLOAD_TOO_LARGE
        429 -> DataError.Network.TOO_MANY_REQUESTS
        500 -> DataError.Network.SERVER_ERROR
        else -> errorBody?.let {
            if (it.reason != null) DataError.CustomError(it.reason) else DataError.Network.UNKNOWN
        } ?: DataError.Network.UNKNOWN
    }
}
fun HttpException.mapError(): DataError.Network {
    return when (code()) {
        408 -> DataError.Network.REQUEST_TIMEOUT
        413 -> DataError.Network.PAYLOAD_TOO_LARGE
        429 -> DataError.Network.TOO_MANY_REQUESTS
        500 -> DataError.Network.SERVER_ERROR
        else -> DataError.Network.UNKNOWN
    }
}

