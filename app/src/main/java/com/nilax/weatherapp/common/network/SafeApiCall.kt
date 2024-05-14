package com.nilax.weatherapp.common.network

import com.nilax.weatherapp.domain.model.error.DataError
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T, DataError.Network> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                Result.Success(it)
            } ?: Result.Error(DataError.Network.UNKNOWN)
        } else {
            Result.Error(response.mapError())
        }
    } catch (ex: IOException) {
        Result.Error(DataError.Network.NO_INTERNET)
    } catch (ex: HttpException) {
        Result.Error(ex.mapError())
    }
}