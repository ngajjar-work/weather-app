package com.nilax.weatherapp.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LocationValidatorTest {

    private lateinit var sut: LocationValidator

    @Before
    fun setUp() {
        sut = LocationValidator()
    }

    @Test
    fun `isValidLocation should return true for valid location`() {
        assertTrue(sut.isValidLocation("10.0,12"))
    }

    @Test
    fun `isValidLocation should return false for empty location`() {
        assertFalse(sut.isValidLocation(""))
    }

    @Test
    fun `isValidLocation should return false for invalid location format`() {
        assertFalse(sut.isValidLocation("12412."))
    }

    @Test
    fun `getLocationFromString should return correct pair`() {
        val expectedPair = Pair(10.0, 12.0)
        assertEquals(expectedPair, sut.getLocationFromString("10.0,12"))
    }
}
