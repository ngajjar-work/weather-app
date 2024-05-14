package com.nilax.weatherapp.common.network

import com.google.gson.Gson
import com.nilax.weatherapp.data.remote.dto.WeeklyWeatherResponse
import com.nilax.weatherapp.domain.model.error.DataError
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class NetworkErrorMapperTest {


    @Test
    fun `should map 408 Response error to REQUEST_TIMEOUT`() {
        val response = createMockErrorResponse(408)
        assertEquals(DataError.Network.REQUEST_TIMEOUT, response.mapError())
    }

    @Test
    fun `should map 408 HTTP Exception to REQUEST_TIMEOUT`() {
        val response = createMockErrorHttpException(408)
        assertEquals(DataError.Network.REQUEST_TIMEOUT, response.mapError())
    }

    @Test
    fun `should map 413 Response error to PAYLOAD_TOO_LARGE`() {
        val response = createMockErrorResponse(413)
        assertEquals(DataError.Network.PAYLOAD_TOO_LARGE, response.mapError())
    }

    @Test
    fun `should map 413 HTTP Exception to PAYLOAD_TOO_LARGE`() {
        val response = createMockErrorHttpException(413)
        assertEquals(DataError.Network.PAYLOAD_TOO_LARGE, response.mapError())
    }


    @Test
    fun `should map 429 Response error to TOO_MANY_REQUESTS`() {
        val response = createMockErrorResponse(429)
        assertEquals(DataError.Network.TOO_MANY_REQUESTS, response.mapError())
    }

    @Test
    fun `should map 429 HTTP Exception to TOO_MANY_REQUESTS`() {
        val response = createMockErrorHttpException(429)
        assertEquals(DataError.Network.TOO_MANY_REQUESTS, response.mapError())
    }

    @Test
    fun `should map 500 Response error to SERVER_ERROR`() {
        val response = createMockErrorResponse(500)
        assertEquals(DataError.Network.SERVER_ERROR, response.mapError())
    }

    @Test
    fun `should map 500 HTTP Exception to SERVER_ERROR`() {
        val response = createMockErrorHttpException(500)
        assertEquals(DataError.Network.SERVER_ERROR, response.mapError())
    }

    @Test
    fun `should map 401 Response error to UNKNOWN`() {
        val response = createMockErrorResponse(401)
        assertEquals(DataError.Network.UNKNOWN, response.mapError())
    }

    @Test
    fun `should map 401 HTTP Exception to UNKNOWN`() {
        val response = createMockErrorHttpException(401)
        assertEquals(DataError.Network.UNKNOWN, response.mapError())
    }

    private fun createMockErrorResponse(code: Int): Response<WeeklyWeatherResponse> {
        val errorJson = Gson().toJson(null)
        return Response.error(
            code,
            errorJson.toResponseBody("application/json".toMediaTypeOrNull())
        )
    }

    private fun createMockErrorHttpException(code: Int): HttpException {
        val errorResponse = Response.error<Any>(code, ResponseBody.create(null, ""))
        return HttpException(errorResponse)
    }

}