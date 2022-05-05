package com.example.soleklart.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soleklart.ds.locationforecast.ForecastData
import com.example.soleklart.ds.locationforecast.LocationforecastDS
import com.example.soleklart.ds.nilu.NiluDataSource
import com.example.soleklart.ds.sunrise.CompactSunriseData
import com.example.soleklart.ds.sunrise.SunriseDataSource
import com.example.soleklart.utils.currentDate
import com.example.soleklart.utils.filterNotNullValues
import com.example.soleklart.utils.plusHours
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Shared viewmodel for sharing data between fragments.
 * Contains data from all data sources, and methods for updating data based on location and date.
 */
@Suppress("MemberVisibilityCanBePrivate")
class SharedViewModel : ViewModel() {

    // data sources
    private val sunriseDS = SunriseDataSource()
    private val niluDS = NiluDataSource()
    private val locationforecastDS = LocationforecastDS()

    private val _latLong = MutableLiveData<LatLng>()
    val latLong: LiveData<LatLng> = _latLong

    // data fetching state
    private val _state = MutableLiveData<String>()
    val state: LiveData<String> = _state

    private val _date = MutableLiveData<String>().also {
        it.postValue(currentDate())
    }
    val date: LiveData<String> = _date

    /**
     * map of time types (sunrise, sunset, after) to times relevant for data
     */
    private val _times = MutableLiveData<Map<String, String>>()
    val times: LiveData<Map<String, String>> = _times

    private val _sunriseData = MutableLiveData<CompactSunriseData>()
    val sunriseData: LiveData<CompactSunriseData> = _sunriseData

    private val _niluData = MutableLiveData<Map<String, Double?>>()
    val niluData: LiveData<Map<String, Double?>> = _niluData

    private val _forecasts = MutableLiveData<Map<String, ForecastData>>()
    val forecasts: LiveData<Map<String, ForecastData>> = _forecasts

    /**
     * set latLong coordinates with new values, and update data values for new location
     */
    fun setLatLng(latlng: LatLng) {
        _latLong.value = latlng
        updateData()
    }

    /**
     * set date as value, and update data values for new date, if location is set
     */
    fun setDate(date: String) {
        _date.value = date
        state.value?.let {
            updateData()
        }
    }

    /**
     * sets _state value as loading,
     * updates data from all apis with coordinates in _latLong, and sets
     * _state value finished on completion
     */
    private fun updateData() {
        _state.value = "loading"
        viewModelScope.launch {
            fetchSunriseData().join()
            updateTimes()
            val niluJob = updateNilu()
            val forecastJob = updateForecasts()
            niluJob.join()
            forecastJob.join()
            _state.value = "finished"
        }
    }

    /**
     * Updates times relevant for data output from APIs and updates timesMap
     * maps type of time (sunrise, sunset, after) to respective times from sunrise
     */
    private fun updateTimes() {
        val sunriseTime = sunriseData.value?.sunriseTime
        val sunsetTime = sunriseData.value?.sunsetTime
        val afterSunsetTime = sunsetTime?.let { plusHours(it, 2) }
        Log.d("sunriseTime", sunriseTime.toString())
        Log.d("sunsetTime", sunsetTime.toString())
        Log.d("afterSunsetTime", afterSunsetTime.toString())
        val timesMap =
            mapOf("sunrise" to sunriseTime, "sunset" to sunsetTime, "after" to afterSunsetTime)
        _times.value = timesMap.filterNotNullValues() // removes entries where values is null
    }

    //Sunrise
    /**
     * fetches sunrise data from data source & updates _sunriseData with data
     */
    private fun fetchSunriseData(): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            val lat = latLong.value?.latitude ?: 0.0
            val long = latLong.value?.longitude ?: 0.0
            val date = _date.value ?: currentDate() //_date value or current date
            Log.d("Date", date)
            sunriseDS.getCompactSunriseData(lat, long, date).also {
                _sunriseData.postValue(it)
            }
        }
    }


    //Nilu
    /**
     * Updates nilu data and maps time types (sunrise, sunset, after) to data
     */
    private fun updateNilu(): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            val timeValues = times.value
            if (timeValues != null) {
                // creates Pair of time key & double of non null airQuality from timeValues
                val mNilu = timeValues.mapNotNull {
                    val value = fetchNilu(it.value)
                    if (value != null) Pair(it.key, value) else null
                }
                _niluData.postValue(mNilu.toMap()) //returns map of pairs
            }
        }
    }

    /**
     * fetches nilu data from data source & returns value based on timestamp & location
     */
    private suspend fun fetchNilu(time: String): Double? {
        val lat = latLong.value?.latitude ?: 0.0
        val long = latLong.value?.longitude ?: 0.0

        niluDS.fetchNilu(lat, long, 20, time).also {
            return it
        }
    }

    //Locationforecast
    /**
     * Updates forecast data and maps time types (sunrise, sunset, after) to data
     */
    private fun updateForecasts(): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            val timeValues = times.value
            if (timeValues != null) {
                // creates Pair of time key & list of non null forecasts from timeValues
                val mForecasts = timeValues.mapNotNull {
                    val value = fetchForecast(it.value)
                    if (value != null) Pair(it.key, value) else null
                }
                _forecasts.postValue(mForecasts.toMap()) //returns map of pairs
            }
        }
    }

    /**
     * Fetching forecast data from datsource and returns value based on timestamp & location
     */
    private suspend fun fetchForecast(time: String): ForecastData? {
        val lat = latLong.value?.latitude ?: 0.0
        val long = latLong.value?.longitude ?: 0.0
        locationforecastDS.getForecast(lat, long, time).also {
            return it
        }
    }

}