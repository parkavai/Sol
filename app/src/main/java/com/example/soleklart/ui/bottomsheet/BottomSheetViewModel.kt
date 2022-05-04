package com.example.soleklart.ui.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.soleklart.ds.locationforecast.ForecastData
import com.example.soleklart.ui.SharedViewModel
import com.example.soleklart.utils.prettyTimeString
import com.example.soleklart.utils.timeTypeToHeader

class BottomSheetViewModel(private val dataSource: SharedViewModel) {

    private val _outData = MutableLiveData<List<OutputData>>()
    val outData: LiveData<List<OutputData>> = _outData

    /**
     * Loads data from all data sources in ViewModelScope Coroutine
     * and updates outData value with List of OutputData data class
     */
    fun loadDataOutput() {
        val forecasts = dataSource.forecasts.value
        val airQuality = dataSource.niluData.value
        val times = dataSource.times.value

        // creates list of OutputData from map of timeType and forecast
        val outArray = forecasts?.map {
            val timeType = it.key
            val forecast = it.value
            createOutputData(forecast, airQuality?.get(timeType), times?.get(timeType), timeType)
        }

        outArray?.let { _outData.postValue(it) }
    }

    /**
     * creates & returns outputData class with params: forecast, airQuality, time & type
     */
    private fun createOutputData(
        forecast: ForecastData,
        airQuality: Double?,
        time: String?,
        type: String
    ): OutputData {
        val missingValue = "-"
        val headerStart = timeTypeToHeader(type)
        val header = "$headerStart ${time?.let { prettyTimeString(it) } ?: missingValue}"
        val temperature = forecast.temperature
        val cloudCover = forecast.cloudCover
        val windSpeed = forecast.wind_speed
        val rain = forecast.precipitation6Hours
        val mAirQuality =
            if (airQuality != null) "%.2f".format(airQuality) + " µg/m³" else missingValue

        return OutputData(header, temperature, cloudCover, windSpeed, rain, mAirQuality)
    }

}