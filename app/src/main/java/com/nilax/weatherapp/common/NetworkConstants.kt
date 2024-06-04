package com.nilax.weatherapp.common

object NetworkConstants {
    //endpoints
    const val BASE_URL = "https://api.open-meteo.com/v1/"
    const val ICON_URL = "https://openweathermap.org/img/wn/%s%s@%s.png"

    //api parameters
    const val CURRENT_PARAMS = "temperature_2m,weather_code,apparent_temperature,is_day"
    const val DAILY_PARAMS = "weather_code,temperature_2m_max,temperature_2m_min"

    //static location
    val DEFAULT_LAT_LNG = Pair(43.7001, -79.4163)


}