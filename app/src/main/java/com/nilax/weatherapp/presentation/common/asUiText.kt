package com.nilax.weatherapp.presentation.common

import com.nilax.weatherapp.R
import com.nilax.weatherapp.common.network.Result
import com.nilax.weatherapp.domain.model.error.DataError

fun DataError.asUiText(): UiText {
    return when (this) {
        DataError.Network.REQUEST_TIMEOUT -> UiText.StringResource(
            R.string.msg_the_request_timed_out
        )

        DataError.Network.TOO_MANY_REQUESTS -> UiText.StringResource(
            R.string.msg_youve_hit_your_rate_limit
        )

        DataError.Network.NO_INTERNET -> UiText.StringResource(
            R.string.msg_no_internet
        )

        DataError.Network.PAYLOAD_TOO_LARGE -> UiText.StringResource(
            R.string.msg_file_too_large
        )

        DataError.Network.SERVER_ERROR -> UiText.StringResource(
            R.string.msg_server_error
        )

        DataError.Network.SERIALIZATION -> UiText.StringResource(
            R.string.msg_error_serialization
        )

        DataError.Network.UNKNOWN -> UiText.StringResource(
            R.string.msg_unknown_error
        )
    }
}

fun Result.Error<*, DataError>.asErrorUiText(): UiText {
    return error.asUiText()
}