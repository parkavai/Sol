package com.example.himmeltitting.locationforecastTests


import com.example.himmeltitting.LocationforecastDS
import kotlinx.coroutines.runBlocking
import org.junit.Test


class DataSourceTest {
    val ls = LocationforecastDS()

    val lat = 60.10
    val lon = 9.58

    @Test
    fun response_isNotNull() {

        runBlocking {
            val result = ls.getAllForecastData(lat, lon)
            assert(result != null)
            print(result.toString())
        }

    }
}


