package com.example.soleklart.ds.locationforecast

import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

const val LAT = 59.9
const val LON = 10.8
const val TIME = "2022-03-30T23:36:14"

class LocationforecastDSTest {

    private lateinit var source: LocationforecastDS

    @Before
    fun setUp() {
        source = LocationforecastDS()
    }

    @Test
    fun getForecast_isNotNull() {
        runBlocking {
            val result = source.getForecast(LAT, LON, TIME)
            assert(result != null)
            print(result.toString())
        }
    }

}