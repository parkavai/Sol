package com.example.himmeltitting

import android.location.Location
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
import com.example.himmeltitting.utils.TimeConversion
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
            "${TimeConversion().prettyTimeString(sunriseData.time)}: ${sunriseData.temperature}, " +
                    "${sunriseData.cloudCover} cloud, ${sunriseData.wind_speed} wind\n" +
                    "${TimeConversion().prettyTimeString(sunsetData.time)}: ${sunsetData.temperature}, " +
                    "${sunsetData.cloudCover} cloud, ${sunsetData.wind_speed} wind"
        }
    }

    /**
     * Creates and return String with Air Quality data from LatLng coordinates
     */
    private suspend fun getAirQualityString(): String {
        val data = getNilu().value

        val closestData = getClosestAirQuality(data)

        return if (closestData == null) {
            return "Fant ikke luftkvalitet"
        } else {
            "Luftkvalitet: ${closestData.value}"
        }


    }

    /**
     * Returns Luftkvalitet Data class with closest Air Station for data
     */
    private fun getClosestAirQuality(list: List<LuftKvalitet>?): LuftKvalitet? {
        val lat = latLong.value?.latitude ?: 0.0
        val long = latLong.value?.longitude ?: 0.0
        val currentLocation: Location = createLocation(lat, long)
        var output: LuftKvalitet? = null
        var smallestDistance = 100000.0.toFloat()
        list?.forEach {
            val location = it.latitude?.let { it1 ->
                it.longitude?.let { it2 ->
                    createLocation(it1, it2)
                }
            }
            val distance = currentLocation.distanceTo(location)
            if (distance < smallestDistance) {
                smallestDistance = distance
                output = it
            }
        }
        return output
    }

    /**
     * helper function to create Location data
     */
    private fun createLocation(latitude: Double, longitude: Double): Location {
        val location = Location("")
        location.latitude = latitude
        location.longitude = longitude
        return location
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
            "Solnedgang: ${TimeConversion().prettyTimeString(data.sunsetTime!!)}\n" +
                    "Soloppgang: ${TimeConversion().prettyTimeString(data.sunriseTime!!)}"
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
            val date = "2022-03-31" //will be from calendar
            sunriseDS.getCompactSunriseData(lat, long, date).also {
                sunriseData.postValue(it)
            }
        }
    }

    //Nilu
    private val niluData = MutableLiveData<List<LuftKvalitet>>()

    /**
     * loads data from Nilu datasource, and waits for coroutine to finish, before returning data
     */
    private suspend fun getNilu(): LiveData<List<LuftKvalitet>> {
        fetchNilu(20).join()
        return niluData
    }

    private fun fetchNilu(radius: Int): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            val lat = latLong.value?.latitude ?: 0.0
            val long = latLong.value?.longitude ?: 0.0
            niluDS.fetchNilusMedRadius(lat, long, radius).also {
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
        val time = sunriseData.value?.sunriseTime ?: TimeConversion().currentTime()
        loadForecast(sunriseForecast, time).join()
        return sunriseForecast

    }

    private val sunsetForecast: MutableLiveData<CompactTimeSeriesData?> by lazy {
        MutableLiveData<CompactTimeSeriesData?>()
    }

    /**
     * loads data from forecast datasource, and waits for coroutine to finish, before returning data
     */
    private suspend fun getSunsetForecast(): LiveData<CompactTimeSeriesData?> {
        val time = sunriseData.value?.sunsetTime ?: TimeConversion().currentTime()
        loadForecast(sunsetForecast, time).join()
        return sunsetForecast
    }

    private fun loadForecast(forecast: MutableLiveData<CompactTimeSeriesData?>, time: String): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            val lat = latLong.value?.latitude ?: 0.0
            val long = latLong.value?.longitude ?: 0.0
            locationforecastDS.getCompactTimeseriesData(lat, long, time).also {
                forecast.postValue(it)
            }
        }
    }

}