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
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MapsActivityViewModel : ViewModel() {

    private val sunriseDS = SunRiseDataSource()
    private val niluDS = NiluDataSource()
    private val locationforecastDS = LocationforecastDS()

    private val outData : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    /**
     * Takes LatLng with coordinates, and returns a string of data for the location
     */
    fun getDataOutput(latLng: LatLng): LiveData<String> {
        loadDataOutput(latLng)
        return outData
    }

    /**
     * Loads strings from all data sources in ViewModelScope Coroutine
     * and sets outText value in outData Livedata
     */
    private fun loadDataOutput(latLng: LatLng) {
        viewModelScope.launch(Dispatchers.IO) {
            val sunriseString = getSunriseString(latLng)
            val airQualityString = getAirQualityString(latLng)
            val forecastString = getForecastString(latLng)


            val outText = forecastString + "\n" + airQualityString + "\n" + sunriseString
            outData.postValue(outText)
        }
    }

    /**
     * Creates and return String with forecast data from LatLng coordinates
     */
    private suspend fun getForecastString(latLng: LatLng): String{
        val changedData = getCompactForecast(latLng.latitude, latLng.longitude)
        val data = changedData.value
        return if (data == null){
            "Kunne ikke hente Forecast data"
        }else{
            "Nå (${data.time}):\n" +
                    "Temperatur: ${data.temperature}, Skydekke: ${data.cloudCover}, Vindhastighet: ${data.wind_speed}\n" +
                    "Precipation neste 6 timene: ${data.precipitation6Hours}\n" +
                    "SymbolSummary neste 12 timer: ${data.summary12Hour}"
        }
    }

    /**
     * Creates and return String with Air Quality data from LatLng coordinates
     */
    private suspend fun getAirQualityString(latLng: LatLng): String{
        val data = getNilu(latLng.latitude, latLng.longitude).value

        val closestData = getClosestAirQuality(latLng, data)

        return if (closestData == null) {
            return "Fant ikke luftkvalitet"
        } else{
            "Luftkvalitet: ${closestData.value}"
        }


    }

    /**
     * Returns Luftkvalitet Data class with closest Air Station for data
     */
    private fun getClosestAirQuality(latLng: LatLng, list: List<LuftKvalitet>?): LuftKvalitet? {
        val currentLocation: Location = createLocation(latLng.latitude, latLng.longitude)
        var output: LuftKvalitet? = null
        var smallestDistance = 100000.0.toFloat()
        list?.forEach {
            val location = it.latitude?.let { it1 -> it.longitude?.let { it2 ->
                createLocation(it1, it2)
            } }
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
    private fun createLocation(latitude : Double, longitude : Double) : Location{
        val location = Location("")
        location.latitude = latitude
        location.longitude = longitude
        return location
    }


    /**
     * Creates and return String with Sunrise data from LatLng coordinates
     */
    private suspend fun getSunriseString(latLng: LatLng): String {
        val data = getSunriseData(latLng.latitude, latLng.longitude).value

        //returns sunrise data as string if data is not null, else returns not found string
        return if (data == null) {
            "Fant ikke solnedgang"
        }else{
            "Solnedgang: ${data.sunsetTime}"
        }
    }

    //Sunrise
    private val sunriseData = MutableLiveData<CompactSunriseData>()

    /**
     * loads data from forecast datasource, and waits for coroutine to finish, before returning data
     */
    private suspend fun getSunriseData(lat: Double, long: Double): MutableLiveData<CompactSunriseData> {
        fetchSunriseData(lat, long).join()
        return sunriseData
    }

    private fun fetchSunriseData(lat: Double, long: Double): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            sunriseDS.getCompactSunriseData(lat, long).also{
                sunriseData.postValue(it)
            }
        }
    }

    //Nilu
    private val niluData = MutableLiveData<List<LuftKvalitet>>()

    /**
     * loads data from Nilu datasource, and waits for coroutine to finish, before returning data
     */
    private suspend fun getNilu(lat: Double, long: Double): LiveData<List<LuftKvalitet>> {
        fetchNilu(lat, long, 20).join()
        return niluData
    }

    private fun fetchNilu(latitude: Double?, longitude: Double?, radius: Int): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            niluDS.fetchNilusMedRadius(latitude, longitude, radius).also {
                niluData.postValue(it)
            }
        }
    }

    //Locationforecast

    private val compactForecastData: MutableLiveData<CompactTimeSeriesData?> by lazy {
        MutableLiveData<CompactTimeSeriesData?>()
    }

    /**
     * loads data from forecast datasource, and waits for coroutine to finish, before returning data
     */
    private suspend fun getCompactForecast(lat: Double, lon: Double): LiveData<CompactTimeSeriesData?> {
        loadCompactForecast(lat, lon).join()
        return compactForecastData

    }

    private fun loadCompactForecast(lat: Double, lon: Double): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            locationforecastDS.getCompactTimeseriesData(lat, lon).also {
                compactForecastData.postValue(it)
            }
        }
    }

}