package com.example.soleklart.imageConversionTests

import com.example.soleklart.utils.cloudImageCalculator
import org.junit.Test

class CloudImageConversionTest {

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