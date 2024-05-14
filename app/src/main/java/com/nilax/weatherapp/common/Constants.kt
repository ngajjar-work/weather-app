package com.nilax.weatherapp.common

import java.text.SimpleDateFormat

object Constants {
    const val BASE_URL = "https://api.open-meteo.com/v1/"

    val sdfServerDate = SimpleDateFormat("yyyy-MM-dd")
    val sdfServerDateTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
    val sdfDisplayDay = SimpleDateFormat("EEEE")

}