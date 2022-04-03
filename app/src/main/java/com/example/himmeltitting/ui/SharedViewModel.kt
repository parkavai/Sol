package com.example.himmeltitting.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.himmeltitting.ds.locationforecast.CompactTimeSeriesData
import com.example.himmeltitting.ds.locationforecast.LocationforecastDS
import com.example.himmeltitting.ds.nilu.LuftKvalitet
import com.example.himmeltitting.ds.nilu.NiluDataSource
import com.example.himmeltitting.ds.sunrise.CompactSunriseData
import com.example.himmeltitting.ds.sunrise.SunRiseDataSource
import com.example.himmeltitting.utils.currentDate
import com.example.himmeltitting.utils.currentTime
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {

    private val sunriseDS = SunRiseDataSource()
    private val niluDS = NiluDataSource()
    private val locationforecastDS = LocationforecastDS()

    private val _latLong = MutableLiveData<LatLng>()
    val latLong : LiveData<LatLng> = _latLong

    private val _state = MutableLiveData<String>()
    val state : LiveData<String> = _state

    private val _date = MutableLiveData<String>().also {
        it.postValue(currentDate())
    }
    val date : LiveData<String> = _date

    private val _sunriseData = MutableLiveData<CompactSunriseData>()
    val sunriseData : LiveData<CompactSunriseData> = _sunriseData

    private val _niluData = MutableLiveData<LuftKvalitet>()
    val niluData : LiveData<LuftKvalitet> = _niluData

    private val _sunriseForecast = MutableLiveData<CompactTimeSeriesData?>()
    val sunriseForecast : LiveData<CompactTimeSeriesData?> = _sunriseForecast

    private val _sunsetForecast = MutableLiveData<CompactTimeSeriesData?>()
    val sunsetForecast : LiveData<CompactTimeSeriesData?> = _sunsetForecast

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
        viewModelScope.launch{
            fetchNilu(20)
            fetchSunriseData().join()
            updateForecasts().join()
            _state.value = "finished"
        }
    }

    //Sunrise
    private fun fetchSunriseData(): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            val lat = latLong.value?.latitude ?: 0.0
            val long = latLong.value?.longitude ?: 0.0
            val date = _date.value?: currentDate() //_date value or current date
            Log.d("Date", date)
            sunriseDS.getCompactSunriseData(lat, long, date).also {
                _sunriseData.postValue(it)
            }
        }
    }

    //Nilu
    private fun fetchNilu(radius: Int): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            val lat = latLong.value?.latitude ?: 0.0
            val long = latLong.value?.longitude ?: 0.0
            niluDS.fetchNilu(lat, long, radius).also {
                _niluData.postValue(it)
            }
        }
    }

    //Locationforecast
    /**
     * updates forecast data with sunrise and sunset times for location
     */
    private fun updateForecasts(): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            val sunriseTime = sunriseData.value?.sunriseTime ?: currentTime()
            val sunsetTime = sunriseData.value?.sunsetTime ?: currentTime()

            fetchForecast(_sunriseForecast, sunriseTime).join()
            fetchForecast(_sunsetForecast, sunsetTime).join()
        }
    }

    private fun fetchForecast(forecast: MutableLiveData<CompactTimeSeriesData?>, time: String): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            val lat = latLong.value?.latitude ?: 0.0
            val long = latLong.value?.longitude ?: 0.0
            locationforecastDS.getCompactTimeseriesData(lat, long, time).also {
                forecast.postValue(it)
            }
        }
    }

}