package com.example.himmeltitting.locationforecastTests

import com.example.himmeltitting.MapsActivityViewModel
import com.example.himmeltitting.locationforecast.LocationforecastDS
import kotlinx.coroutines.runBlocking
import org.junit.Test

class TimeTest {

    val viewModel = MapsActivityViewModel()

    @Test
    fun currentTime_isNotNull() {
        val time = viewModel.currentTime()
        print(time)
    }

    @Test
    fun convertTime_isNotNull() {
        val time = viewModel.sunTimeToForecastTime("2022-03-30T14:36:14+02:00")
        print(time)
    }

}