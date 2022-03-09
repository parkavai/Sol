package com.example.himmeltitting.LocationforecastTests


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
            val result = ls.getData(lat, lon)
            assert(result.geometry != null && result.properties != null && result.type!=null)
            print(result.toString())
        }

    }
}


