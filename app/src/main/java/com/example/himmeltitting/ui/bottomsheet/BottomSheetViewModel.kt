package com.example.himmeltitting.ui.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.himmeltitting.ds.locationforecast.CompactTimeSeriesData
import com.example.himmeltitting.ui.SharedViewModel
import com.example.himmeltitting.utils.prettyTimeString

class BottomSheetViewModel(private val dataSource: SharedViewModel) {

    private val _outData = MutableLiveData<List<ForecastData>>()
    val outData : LiveData<List<ForecastData>> = _outData

    /**
     * Loads strings from all data sources in ViewModelScope Coroutine
     * and updates outData value in outData Livedata
     */
    fun loadDataOutput() {
        val sunriseForecastValue = dataSource.sunriseForecast.value
        val sunsetForecastValue = dataSource.sunsetForecast.value

        val niluSunrise = dataSource.niluData.value?.airQualitySunrise
        val niluSunset= dataSource.niluData.value?.airQualitySunset
        val sunriseTime = dataSource.sunriseData.value?.sunriseTime
        val sunsetTime = dataSource.sunriseData.value?.sunsetTime

        val sunriseForecastData = createForecastData(sunriseForecastValue, niluSunrise, sunriseTime, "Soloppgang:")
        val sunsetForecastData = createForecastData(sunsetForecastValue, niluSunset, sunsetTime, "Solnedgang:")


        val outArray = listOf(sunriseForecastData, sunsetForecastData)
        _outData.postValue(outArray)
    }

    private fun createForecastData(forecast: CompactTimeSeriesData?, niluVal: String?, sunTime: String?, headerStart: String) : ForecastData {
        val missingValue = "-"
        val header = "$headerStart ${sunTime?.let { prettyTimeString(it) }}"
        val temperature = forecast?.temperature ?: missingValue
        val cloudCover = forecast?.cloudCover ?: missingValue
        val windSpeed = forecast?.wind_speed ?: missingValue
        val rain = forecast?.precipitation6Hours ?: missingValue
        val airQuality = niluVal ?: missingValue

        return ForecastData(header, temperature, cloudCover, windSpeed, rain, airQuality)
    }
}