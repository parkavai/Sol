package com.example.soleklart.utils

import org.junit.Before
import org.junit.Test

class ImageHelperTest {

    @Before
    fun setUp() {
    }

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

    /**
     * Tests if right image is returned based on data value
     */
    @Test
    fun correctImageSun() {
        val cloud =  cloudImageCalculator("5")
        assert(cloud == "@drawable/sun")
    }

    /**
     * Tests if right image is returned based on data value
     */
    @Test
    fun correctImageCloudSun() {
        val cloud =  cloudImageCalculator("23")
        assert(cloud == "@drawable/cloudsun")
    }

    /**
     * Tests if right image is returned based on data value
     */
    @Test
    fun correctImageCloud() {
        val cloud =  cloudImageCalculator("100")
        assert(cloud == "@drawable/clouds")
    }
}