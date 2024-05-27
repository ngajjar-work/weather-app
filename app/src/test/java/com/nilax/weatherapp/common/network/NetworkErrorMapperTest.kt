package com.nilax.weatherapp.common.network

import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.whenever
import com.nilax.weatherapp.data.remote.dto.WeeklyWeatherResponse
import com.nilax.weatherapp.domain.model.error.DataError
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito
import retrofit2.HttpException
import retrofit2.Response
import java.io.StringReader

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

    @Test
    fun `should return custom error for valid error body`() = runTest {

        // Arrange
        val errorJson = "{\"error\": true,\"reason\": \"Latitude must be in range of -90 to 90\"}"

        val mockErrorResponse: Response<WeeklyWeatherResponse> = Response.error(
            400,
            errorJson.toResponseBody("application/json".toMediaTypeOrNull())
        )

        // Act
        val error = mockErrorResponse.mapError()

        // Assert
        assertTrue(error is DataError.CustomError)
        assertEquals(
            "Latitude must be in range of -90 to 90",
            (error as DataError.CustomError).message
        )
    }


    @Test
    fun `should return UNKNOWN error for unexpected error body`() = runTest {

        val errorJson = "{\"message\": \"Unexpected Error Occurred\"}"

        val mockErrorResponse: Response<WeeklyWeatherResponse> = Response.error(
            400,
            errorJson.toResponseBody("application/json".toMediaTypeOrNull())
        )

        assertEquals(DataError.Network.UNKNOWN, mockErrorResponse.mapError())
    }

    @Test
    fun `getErrorObject should return null on JsonSyntaxException`() {

        val brokenJson = "This is not valid JSON"
        val mockResponseBody: ResponseBody = Mockito.mock(ResponseBody::class.java)

        // Mock response body to return broken JSON as string
        whenever(mockResponseBody.charStream()).thenReturn(StringReader(brokenJson))

        // Assert null is returned when parsing fails
        Assert.assertNull(mockResponseBody.getErrorObject<Any>())
    }

}