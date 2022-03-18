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
import kotlinx.coroutines.launch

class MapsActivityViewModel : ViewModel() {

    private val sunriseDS = SunRiseDataSource()
    private val niluDS = NiluDataSource()
    private val locationforecastDS = LocationforecastDS()

    //private val outData = MutableLiveData<String>()


    fun getDataOutPut(latLng: LatLng): MutableLiveData<String> {
        val sunriseString = getSunriseString(latLng)
        val airQualityString = getAirQualityString(latLng)
        val forecastString = getForecastString(latLng)


        val outText = forecastString + "\n" + airQualityString + "\n" + sunriseString
        return MutableLiveData(outText)
    }

    private fun getForecastString(latLng: LatLng): String{
        val data = getCompactForecast(latLng.latitude, latLng.longitude).value
        return if (data == null){
            "Kunne ikke hente Forecast data"
        }else{
            "Nå (${data.time}):\n" +
                    "Temperatur: ${data.temperature}, Skydekke: ${data.cloudCover}, Vindhastighet: ${data.wind_speed}\n" +
                    "Precipation neste 6 timene: ${data.precipitation6Hours}\n" +
                    "SymbolSummary neste 12 timer: ${data.summary12Hour}"
        }
    }

    private fun getAirQualityString(latLng: LatLng): String{
        val data = getNilu(latLng.latitude, latLng.longitude).value

        val closestData = getClosestAirQuality(latLng, data)

        return if (closestData == null) {
            return "Fant ikke luftkvalitet"
        } else{
            "Luftkvalitet: ${closestData.value}"
        }


    }

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

    private fun createLocation(latitude : Double, longitude : Double) : Location{
        val location = Location("")
        location.latitude = latitude
        location.longitude = longitude
        return location
    }


    private fun getSunriseString(latLng: LatLng): String {
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

    private fun getSunriseData(lat: Double, long: Double): MutableLiveData<CompactSunriseData> {
        fetchSunriseData(lat, long)
        return sunriseData
    }

    private fun fetchSunriseData(lat: Double, long: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            sunriseDS.getCompactSunriseData(lat, long).also{
                sunriseData.postValue(it)
            }
        }
    }

    //Nilu
    private val niluData = MutableLiveData<List<LuftKvalitet>>()

    private fun getNilu(lat: Double, long: Double): LiveData<List<LuftKvalitet>> {
        fetchNilu(lat, long, 20)
        return niluData
    }

    private fun fetchNilu(latitude: Double?, longitude: Double?, radius: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            niluDS.fetchNilusMedRadius(latitude, longitude, radius).also {
                niluData.postValue(it)
            }
        }
    }

    //Locationforecast

    private val compactForecastData: MutableLiveData<CompactTimeSeriesData?> by lazy {
        MutableLiveData<CompactTimeSeriesData?>()
    }

    private fun getCompactForecast(lat: Double, lon: Double): LiveData<CompactTimeSeriesData?> {
        loadCompactForecast(lat, lon)
        return compactForecastData
    }

    private fun loadCompactForecast(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            locationforecastDS.getCompactTimeseriesData(lat, lon).also {
                compactForecastData.postValue(it)
            }
        }
    }

}