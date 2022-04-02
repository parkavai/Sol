package com.example.himmeltitting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.himmeltitting.locationforecast.CompactTimeSeriesData
import com.example.himmeltitting.locationforecast.LocationforecastDS
import com.example.himmeltitting.nilu.LuftKvalitet
import com.example.himmeltitting.nilu.NiluDataSource
import com.example.himmeltitting.sunrise.CompactSunriseData
import com.example.himmeltitting.sunrise.SunRiseDataSource
import com.example.himmeltitting.utils.currentTime
import com.example.himmeltitting.utils.prettyTimeString
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MapsActivityViewModel : ViewModel() {

    private val sunriseDS = SunRiseDataSource()
    private val niluDS = NiluDataSource()
    private val locationforecastDS = LocationforecastDS()

    private val outData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val latLong: MutableLiveData<LatLng> by lazy {
        MutableLiveData<LatLng>()
    }

    /**
     * returns a string of data for the current location
     */
    fun getDataOutput(): LiveData<String> {
        return outData
    }

    fun getLatLng(): LiveData<LatLng> {
        return latLong
    }

    fun setLatLng(latlng: LatLng) {
        latLong.value = latlng
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
            outData.postValue(outText)
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
    private val sunriseData = MutableLiveData<CompactSunriseData>()

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
            val date = "2022-04-02" //will be from calendar
            sunriseDS.getCompactSunriseData(lat, long, date).also {
                sunriseData.postValue(it)
            }
        }
    }

    //Nilu
    private val niluData = MutableLiveData<LuftKvalitet>()

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
                niluData.postValue(it)
            }
        }
    }

    //Locationforecast

    private val sunriseForecast: MutableLiveData<CompactTimeSeriesData?> by lazy {
        MutableLiveData<CompactTimeSeriesData?>()
    }

    /**
     * loads data from forecast datasource, and waits for coroutine to finish, before returning data
     */
    private suspend fun getSunriseForecast(): LiveData<CompactTimeSeriesData?> {
        val time = sunriseData.value?.sunriseTime ?: currentTime()
        fetchForecast(sunriseForecast, time).join()
        return sunriseForecast

    }

    private val sunsetForecast: MutableLiveData<CompactTimeSeriesData?> by lazy {
        MutableLiveData<CompactTimeSeriesData?>()
    }

    /**
     * loads data from forecast datasource, and waits for coroutine to finish, before returning data
     */
    private suspend fun getSunsetForecast(): LiveData<CompactTimeSeriesData?> {
        val time = sunriseData.value?.sunsetTime ?: currentTime()
        fetchForecast(sunsetForecast, time).join()
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