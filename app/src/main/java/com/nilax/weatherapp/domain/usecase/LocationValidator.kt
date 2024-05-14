package com.nilax.weatherapp.domain.usecase

import javax.inject.Inject

class LocationValidator @Inject constructor() {

    fun isValidLocation(locationString: String): Boolean {
        if (locationString.isBlank()) return false
        val parts = locationString.split(",")
        if (parts.size != 2) return false

        return parts.all { part -> part.trim().toDoubleOrNull() != null }
    }

    fun getLocationFromString(locationString: String): Pair<Double, Double> {
        val parts = locationString.split(",")
        return Pair(parts[0].trim().toDouble(), parts[1].trim().toDouble())
    }
}