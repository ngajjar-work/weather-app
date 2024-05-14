package com.nilax.weatherapp


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.nilax.weatherapp.presentation.navigation.WeatherNavHost

@Composable
fun WeatherApp(navController: NavHostController = rememberNavController()) {
    WeatherNavHost(navController = navController)
}