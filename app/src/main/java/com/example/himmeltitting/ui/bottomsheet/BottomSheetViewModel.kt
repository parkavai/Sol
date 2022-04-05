package com.example.himmeltitting.ui.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.himmeltitting.ui.SharedViewModel
import com.example.himmeltitting.utils.prettyTimeString

class BottomSheetViewModel(private val dataSource: SharedViewModel) {

    private val _outData = MutableLiveData<String>()
    val outData : LiveData<String> = _outData

    /**
     * Loads strings from all data sources in ViewModelScope Coroutine
     * and updates outData value in outData Livedata
     */
    fun loadDataOutput() {
        val sunriseString = getSunriseString()
        val airQualityString = getAirQualityString()
        val forecastString = getForecastString()

        val outText = sunriseString + "\n" + airQualityString + "\n" + forecastString
        _outData.postValue(outText)
    }

    /**
     * Creates and return String with forecast data from LatLng coordinates
     */
    private fun getForecastString(): String {
        val sunriseData = dataSource.sunriseForecast.value
        val sunsetData = dataSource.sunsetForecast.value
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
    private fun getAirQualityString(): String {
        val data = dataSource.niluData.value

        return if (data == null) {
            return "Fant ikke luftkvalitet"
        } else {
            "Luftkvalitet: ${data.value}"
        }
    }

    /**
     * Creates and return String with Sunrise data from LatLng coordinates
     */
    private fun getSunriseString(): String {
        val data = dataSource.sunriseData.value

        //returns sunrise data as string if data is not null, else returns not found string
        return if (data == null) {
            "Fant ikke solnedgang"
        } else {
            "Solnedgang: ${prettyTimeString(data.sunsetTime!!)}\n" +
                    "Soloppgang: ${prettyTimeString(data.sunriseTime!!)}"
        }
    }
}