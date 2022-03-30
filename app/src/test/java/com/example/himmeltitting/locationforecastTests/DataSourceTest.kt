package com.example.himmeltitting.locationforecastTests


import com.example.himmeltitting.locationforecast.LocationforecastDS
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.system.exitProcess


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

    @Test
    fun closestForeCastData_isNotNull() {

        runBlocking {
            val result = ls.getCompactTimeseriesData(lat, lon, "2022-03-30T23:36:14")
            assert(result != null)
            if (result == null) exitProcess(0)
            print(
                "Time: ${result.time}\nCloudCover: ${result.cloudCover} \nTemperature: ${result.temperature}"
            )
        }

    }
}


