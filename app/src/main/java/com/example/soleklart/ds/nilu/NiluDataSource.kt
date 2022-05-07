package com.example.soleklart.ds.nilu

import android.location.Location
import com.example.soleklart.utils.currentDate
import com.example.soleklart.utils.prettyTimeString
import com.example.soleklart.utils.yesterdaysDate
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Data source for contact with LocationForecast API.
 * Queries Api for forecast information like temperature, cloud cover, rain & wind speed
 */
class NiluDataSource {

    /**
     * Based on (latitude and longitude) + radius, you can cet data for air quality from measuring stations
     */
    //Basert på kordinater + radius, så kan man finne stasjoner hvor det er målt luftkvalitet
    suspend fun fetchNilu(latitude: Double, longitude: Double, radius: Int, time: String): Double? {
        val path = "https://api.nilu.no/aq/historical"

        //get todays and yesterdays date -- need to be changed when we can actual user input
        val yesterday = yesterdaysDate()
        val today = currentDate()

        // get pretty string representation of time
        val mTime = "T" + prettyTimeString(time)

        // make API call paths for time
        val param =
            "/$yesterday$mTime/$today$mTime/$latitude/$longitude/$radius?method=within&components=pm10"

        val gson = Gson()


        return try {
            // https://api.nilu.no/
            val liste = object : TypeToken<List<AirQuality>>() {}.type
            // get a list of result
            val airQualityList: List<AirQuality> = gson.fromJson(
                Fuel.get(path + param).header(Headers.USER_AGENT, "Gruppe 4").awaitString(), liste
            )

            // get closest station for each of the result list
            val airQuality =
                getClosestAirQuality(airQualityList, latitude, longitude)?.values?.get(0)?.value

            //make a simple return object which includes only the
            airQuality?.toDouble()


        } catch (exception: Exception) {
            println("A network request exception was thrown: ${exception.message}")
            null
        }
    }


    /**
     * Returns Luftkvalitet Data class with closest Air Station for data
     */
    private fun getClosestAirQuality(
        list: List<AirQuality>?,
        lat: Double,
        long: Double
    ): AirQuality? {
        val currentLocation: Location = createLocation(lat, long)
        var output: AirQuality? = null
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
}

