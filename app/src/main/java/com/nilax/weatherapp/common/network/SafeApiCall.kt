package com.nilax.weatherapp.common.network

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.nilax.weatherapp.domain.model.error.DataError
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T, DataError> {
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

inline fun <reified T> ResponseBody.getErrorObject(): T? {
    val gson = Gson()
    return try {
        gson.fromJson(charStream().readText(), T::class.java)
    } catch (e: JsonSyntaxException) {
        e.printStackTrace()
        null
    }
}
