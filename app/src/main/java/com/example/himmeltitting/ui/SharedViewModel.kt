package com.example.himmeltitting

import android.util.Log
import androidx.lifecycle.*
import com.example.himmeltitting.ds.locationforecast.CompactTimeSeriesData
import com.example.himmeltitting.ds.locationforecast.LocationforecastDS
import com.example.himmeltitting.ds.nilu.LuftKvalitet
import com.example.himmeltitting.ds.nilu.NiluDataSource
import com.example.himmeltitting.ds.sunrise.CompactSunriseData
import com.example.himmeltitting.ds.sunrise.SunRiseDataSource
import com.example.himmeltitting.utils.currentDate
import com.example.himmeltitting.utils.currentTime
import com.example.himmeltitting.utils.prettyTimeString
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {

    private val sunriseDS = SunRiseDataSource()
    private val niluDS = NiluDataSource()
    private val locationforecastDS = LocationforecastDS()

    private val _outData = MutableLiveData<String>()
    val outData : LiveData<String> = _outData

    private val _latLong = MutableLiveData<LatLng>()
    val latLong : LiveData<LatLng> = _latLong

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
        loadDataOutput()
    }

    /**
     * Loads strings from all data sources in ViewModelScope Coroutine
     * and sets outText value in outData Livedata
     */
    private fun loadDataOutput() {
        viewModelScope.launch(Dispatchers.IO) {
            val sunriseString = getSunriseString()
            val airQualityString = getAirQualityString()
            val forecastString = getForecastString()


            val outText = sunriseString + "\n" + airQualityString + "\n" + forecastString
            _outData.postValue(outText)
        }
    }

    /**
     * Creates and return String with forecast data from LatLng coordinates
     */
    private suspend fun getForecastString(): String {
        val sunriseLiveData = getSunriseForecast()
        val sunsetLiveData = getSunsetForecast()
        val sunriseData = sunriseLiveData.value
        val sunsetData = sunsetLiveData.value
        return if (sunriseData == null || sunsetData == null) {
            "Kunne ikke hente Forecast data"
        } else {
            "${prettyTimeString(sunriseData.time)}: ${sunriseData.temperature}, " +
                    "${sunriseData.cloudCover} cloud, ${sunriseData.wind_speed} wind\n" +
                    "${prettyTimeString(sunsetData.time)}: ${sunsetData.temperature}, " +
                    "${sunsetData.cloudCover} cloud, ${sunsetData.wind_speed} wind"
        }
    }

    /**
     * Creates and return String with Air Quality data from LatLng coordinates
     */
    private suspend fun getAirQualityString(): String {
        val data = getNilu().value

        return if (data == null) {
            return "Fant ikke luftkvalitet"
        } else {
            "Luftkvalitet: ${data.value}"
        }


    }

    /**
     * Creates and return String with Sunrise data from LatLng coordinates
     */
    private suspend fun getSunriseString(): String {
        val data = getSunriseData().value

        //returns sunrise data as string if data is not null, else returns not found string
        return if (data == null) {
            "Fant ikke solnedgang"
        } else {
            "Solnedgang: ${prettyTimeString(data.sunsetTime!!)}\n" +
                    "Soloppgang: ${prettyTimeString(data.sunriseTime!!)}"
        }
    }

    //Sunrise

    /**
     * loads data from forecast datasource, and waits for coroutine to finish, before returning data
     */
    private suspend fun getSunriseData(): LiveData<CompactSunriseData> {
        fetchSunriseData().join()
        return sunriseData
    }

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
    /**
     * loads data from Nilu datasource, and waits for coroutine to finish, before returning data
     */
    private suspend fun getNilu(): LiveData<LuftKvalitet> {
        fetchNilu(20).join()
        return niluData
    }

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
     * loads data from forecast datasource, and waits for coroutine to finish, before returning data
     */
    private suspend fun getSunriseForecast(): LiveData<CompactTimeSeriesData?> {
        val time = sunriseData.value?.sunriseTime ?: currentTime()
        fetchForecast(_sunriseForecast, time).join()
        return sunriseForecast

    }

    /**
     * loads data from forecast datasource, and waits for coroutine to finish, before returning data
     */
    private suspend fun getSunsetForecast(): LiveData<CompactTimeSeriesData?> {
        val time = sunriseData.value?.sunsetTime ?: currentTime()
        fetchForecast(_sunsetForecast, time).join()
        return sunsetForecast
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