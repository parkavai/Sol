package com.example.himmeltitting.ds.locationforecast

import com.example.himmeltitting.utils.timeStringToDate
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson
import kotlin.math.abs

class LocationforecastDS {
    private val gson = Gson()

    private var lastLat = 999.0 // value outside of lat range
    private var lastLong = 999.0 // value outside of long range
    private lateinit var lastData : Locationforecast

    private suspend fun fetchForecastData(lat: Double, lon: Double): Locationforecast? {
        //complete?lat=-16.516667&lon=-68.166667&altitude=4150
        // ^eksempel med complete og altitude inkludert
        val path =
            "https://in2000-apiproxy.ifi.uio.no/weatherapi/locationforecast/2.0/compact?lat=${lat}&lon=${lon}"
        val response = fetchData(path)
        val data: Locationforecast = gson.fromJson(response, Locationforecast::class.java)
        return if (data.properties.timeseries.isEmpty()) {
            null
        } else
            data
    }

    /**
     * checks if latitude and longitude is same as in cache
     * returns cached data if same, else fetches new data with coordinates
     */
    private suspend fun getAllForecastData(lat: Double, lon: Double): Locationforecast? {
        return if (lastLat == lat && lastLong == lon) {
            lastData
        } else {
            val currentData = fetchForecastData(lat, lon)
            currentData?.let {
                lastData = it
                lastLat = lat
                lastLong = lon} // updates cached data
            currentData
        }
    }

    suspend fun getCompactTimeseriesData(
        lat: Double,
        lon: Double,
        time: String
    ): CompactTimeSeriesData? {
        val data = getAllForecastData(lat, lon) ?: return null

        val timeSeries = closestTimeseries(data, time)

        return createCompactData(timeSeries, data.properties.meta.units)
    }

    /**
     * takes Locationforecast data class, and time string with format yyyy-MM-dd'T'HH:mm:ss
     * returns closest timeseries object to time supplied
     * If any errors with string to date conversion, returns first timeseries in list
     */
    private fun closestTimeseries(data: Locationforecast, time: String): Timeseries {
        val firstTimeSeries = data.properties.timeseries[0]
        var closestIndex = 0
        val timeValue = timeStringToDate(time)?.time ?: return firstTimeSeries //time as long value
        var currentMin = abs(
            (timeStringToDate(firstTimeSeries.time)?.time ?: return firstTimeSeries) - timeValue
        ) // current min difference

        // compares difference of times as float, and updates closest index and currentMin if
        // difference is smaller
        for ((index, timeseries) in data.properties.timeseries.withIndex()) {
            val timeSeriesTimeValue = timeStringToDate(timeseries.time)?.time // time as long value
            val diff = abs(timeSeriesTimeValue?.minus(timeValue) ?: Long.MAX_VALUE) // difference as long
            if (diff < currentMin) { // closer date timestamp
                currentMin = diff
                closestIndex = index
            } else if(diff != currentMin) { // if diff increases, min is found in sorted list
                    break
                }
        }

        return data.properties.timeseries[closestIndex]

    }


    private fun createCompactData(timeSeries: Timeseries, units: Units): CompactTimeSeriesData {
        val data = timeSeries.data
        val instant = data.instant
        val instantDetails = instant.details
        val hour12 = data.next_12_hours
        val hour6 = data.next_6_hours

        val time = timeSeries.time
        val temperature = instantDetails.air_temperature.toString() + " C"
        val cloudCover =
            instantDetails.cloud_area_fraction.toString() + " " + units.cloud_area_fraction
        val windSpeed = instantDetails.wind_speed.toString() + " " + units.wind_speed
        val summary12Hours = hour12.summary.symbol_code
        val summary6Hours = hour6.summary.symbol_code
        val precipitation6Hours =
            hour6.details.precipitation_amount.toString() + " " + units.precipitation_amount

        return CompactTimeSeriesData(
            time,
            temperature,
            cloudCover,
            windSpeed,
            summary12Hours,
            summary6Hours,
            precipitation6Hours
        )

    }

    //henter data fra url med Fuel
    private suspend fun fetchData(url: String): String? {
        return try {
            // `$ curl --user-agent 'gruppenavn'`
            val request = Fuel.get(url).header(Headers.USER_AGENT, "Gruppe 4")
            //print(request.response())
            request.awaitString()
        } catch (exception: Exception) {
            println("A network request exception was thrown: ${exception.message}")
            null
        }
    }

}