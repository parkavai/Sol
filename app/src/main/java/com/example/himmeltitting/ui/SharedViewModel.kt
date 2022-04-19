package com.example.himmeltitting.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.himmeltitting.ds.locationforecast.ForecastData
import com.example.himmeltitting.ds.locationforecast.LocationforecastDS
import com.example.himmeltitting.ds.nilu.NiluDataSource
import com.example.himmeltitting.ds.sunrise.CompactSunriseData
import com.example.himmeltitting.ds.sunrise.SunRiseDataSource
import com.example.himmeltitting.utils.currentDate
import com.example.himmeltitting.utils.filterNotNullValues
import com.example.himmeltitting.utils.plusHours
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {

    private val sunriseDS = SunRiseDataSource()
    private val niluDS = NiluDataSource()
    private val locationforecastDS = LocationforecastDS()

    private val _latLong = MutableLiveData<LatLng>()
    val latLong: LiveData<LatLng> = _latLong

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
     * updates nilu data and maps time types (sunrise, sunset, after) to data
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

    private suspend fun fetchNilu(time: String): Double? {
        val lat = latLong.value?.latitude ?: 0.0
        val long = latLong.value?.longitude ?: 0.0

        niluDS.fetchNilu(lat, long, 20, time).also {
            return it
        }
    }

    //Locationforecast
    /**
     * updates forecast data and maps time types (sunrise, sunset, after) to data
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

    private suspend fun fetchForecast(time: String): ForecastData? {
        val lat = latLong.value?.latitude ?: 0.0
        val long = latLong.value?.longitude ?: 0.0
        locationforecastDS.getForecast(lat, long, time).also {
            return it
        }
    }

}