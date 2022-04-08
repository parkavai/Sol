package com.example.himmeltitting.ui.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.himmeltitting.ds.locationforecast.ForecastData
import com.example.himmeltitting.ui.SharedViewModel
import com.example.himmeltitting.utils.prettyTimeString
import com.example.himmeltitting.utils.timeTypeToHeader

class BottomSheetViewModel(private val dataSource: SharedViewModel) {

    private val _outData = MutableLiveData<List<OutputData>>()
    val outData : LiveData<List<OutputData>> = _outData

    /**
     * Loads data from all data sources in ViewModelScope Coroutine
     * and updates outData value
     */
    fun loadDataOutput() {

        val forecasts = dataSource.forecasts.value
        val niluVals = dataSource.niluData.value
        val times = dataSource.times.value

        // creates list of OutputData from list of pairs of timetype and forecast
        val outArray = forecasts?.map {
            createOutputData(it.second, niluVals?.get(it.first), times?.get(it.first), it.first)
        }

        outArray?.let {_outData.postValue(it)}
    }

    /**
     * creates & returns outputData class with params: forecast, niluVal, suntime & type
     */
    private fun createOutputData(forecast: ForecastData, niluVal: Double?, sunTime: String?, type: String) : OutputData {
        val missingValue = "-"
        val headerStart = timeTypeToHeader(type)
        val header = "$headerStart ${sunTime?.let { prettyTimeString(it) } ?: missingValue}"
        val temperature = forecast.temperature
        val cloudCover = forecast.cloudCover
        val windSpeed = forecast.wind_speed
        val rain = forecast.precipitation6Hours
        val airQuality = if (niluVal != null) "%.2f".format(niluVal) else missingValue

        return OutputData(header, temperature, cloudCover, windSpeed, rain, airQuality)
    }

}