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
        val sunriseForcastValue = dataSource.sunriseForecast.value
        val sunsetForcastValue = dataSource.sunsetForecast.value

        val niluValue = dataSource.niluData.value?.value
        val sunriseTime = dataSource.sunriseData.value?.sunriseTime
        val sunsetTime = dataSource.sunriseData.value?.sunsetTime

        val sunriseForcastData = createForeCastData(sunriseForcastValue, niluValue, sunriseTime, "Soloppgang:")
        val sunsetForecastData = createForeCastData(sunsetForcastValue, niluValue, sunsetTime, "Solnedgang:")


        val outArray = listOf(sunriseForcastData, sunsetForecastData)
        _outData.postValue(outArray)
    }

    fun createForeCastData(forecast: CompactTimeSeriesData?, niluVal: Double?, sunTime: String?, headerStart: String) : ForecastData {
        val header = "$headerStart ${sunTime?.let { prettyTimeString(it) }}"
        val temperature = forecast?.temperature ?: "None"
        val cloudCover = forecast?.cloudCover ?: "None"
        val windSpeed = forecast?.wind_speed ?: "None"
        val rain = forecast?.precipitation6Hours ?: "None"
        val airQuality = niluVal?.toString() ?: "None"

        return ForecastData(header, temperature, cloudCover, windSpeed, rain, airQuality)
    }
}