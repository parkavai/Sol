package com.example.himmeltitting.locationforecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LSViewModeltmp : ViewModel() {

    private val dataSource = LocationforecastDS()

    private val forecastData: MutableLiveData<Locationforecast> by lazy {
        MutableLiveData<Locationforecast>()
    }

    fun getForecast(lat: Double, lon: Double): LiveData<Locationforecast> {
        loadForecast(lat, lon)
        return forecastData
    }

    private fun loadForecast(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            dataSource.getAllForecastData(lat, lon).also {
                forecastData.postValue(it)
            }
        }
    }

    private val compactForecastData: MutableLiveData<CompactTimeSeriesData?> by lazy {
        MutableLiveData<CompactTimeSeriesData?>()
    }

    fun getCompactForecast(lat: Double, lon: Double): LiveData<CompactTimeSeriesData?> {
        loadCompactForecast(lat, lon)
        return compactForecastData
    }

    private fun loadCompactForecast(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            dataSource.getCompactTimeseriesData(lat, lon).also {
                compactForecastData.postValue(it)
            }
        }
    }

}