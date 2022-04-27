package com.example.soleklart.imageConversionTests

import com.example.soleklart.utils.airQualityImageCalculator
import org.junit.Test

class AirQualityConversionTest {

    /**
     * Tests if right image is returned based on data value
     */
    @Test
    fun correctImageGood() {
        val quality =  airQualityImageCalculator("35.231")
        assert(quality == "@drawable/factory_green")
    }

    /**
     * Tests if right image is returned based on data value
     */
    @Test
    fun correctImageAverage() {
        val quality =  airQualityImageCalculator("86.53")
        assert(quality == "@drawable/factory_orange")
    }

    /**
     * Tests if right image is returned based on data value
     */
    @Test
    fun correctImageBad() {
        val quality =  airQualityImageCalculator("234.2323")
        assert(quality == "@drawable/factory_red")
    }
}