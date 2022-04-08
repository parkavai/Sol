package com.example.himmeltitting.ds.nilu

import android.location.Location
import android.util.Log
import com.example.himmeltitting.utils.currentDate
import com.example.himmeltitting.utils.prettyTimeString
import com.example.himmeltitting.utils.yesterdaysDate
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NiluDataSource {
    //Henter ut alt fra APIet
    suspend fun fetchNiluDefualt(): List<AirQuality>? {
        val path = "https://api.nilu.no/aq/utd"
        val gson = Gson()

        return try {
            // https://api.nilu.no/
            val liste = object : TypeToken<List<AirQuality>>() {}.type
            gson.fromJson(
                Fuel.get(path).header(Headers.USER_AGENT, "Gruppe 4").awaitString(), liste
            )
        } catch (exception: Exception) {
            Log.d("MAIN_ACTIVITY", "A network request exception was thrown: ${exception.message}")
            null
        }
    }

    //Basert på kordinater + radius, så kan man finne stasjoner hvor det er målt luftkvalitet
    suspend fun fetchNilu(latitude: Double, longitude: Double, radius: Int, time: String): Double? {
        val path = "https://api.nilu.no/aq/historical"

        //get todays and yesterdays date -- need to be changed when we can actual user input
        val yesterday = yesterdaysDate()
        val today = currentDate()

        // get pretty string representation of time
        val mTime = "T"+ prettyTimeString(time)

        // make API call paths for time
        val param = "/$yesterday$mTime/$today$mTime/$latitude/$longitude/$radius?method=within&components=no2;pm10"

        val gson = Gson()


        return try {
            // https://api.nilu.no/
            val liste = object : TypeToken<List<AirQuality>>() {}.type
            // get a list of result
            val airQualityList : List<AirQuality> = gson.fromJson(Fuel.get(path + param).header(Headers.USER_AGENT, "Gruppe 4").awaitString(), liste)

            // get closest station for each of the result list
            val airQuality = getClosestAirQuality(airQualityList, latitude, longitude)?.values?.get(0)?.value

            //make a simple return object which includes only the
            airQuality?.toDouble()


        } catch(exception: Exception) {
            Log.d("MAIN_ACTIVITY", "A network request exception was thrown: ${exception.message}")
            null
        }
    }

    /**
     * Returns Luftkvalitet Data class with closest Air Station for data
     */

    private fun getClosestAirQuality(list: List<AirQuality>?, lat: Double, long: Double): AirQuality? {
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

data class AirQuality(val id: Number?, val zone: String?, val municipality: String?, val area: String?, val station: String?, val type: String?, val eoi: String?, val component: String?, val latitude: Double?, val longitude: Double?, val timestep: Number?, val isVisible: Boolean?, val unit: String?, val values: List<Value>?)

// klasse som inneholder verdier målt på alle tidspunkter
data class Value(val fromTime: String?, val toTime: String?, val value: Number?, val qualityControlled: Boolean?, val index: Number?, val color: String?)

// kompakt klasse for enkel retur av verdier
data class CollectiveAirQuality(val type: String, val value: Double)

// Har laget en annen klasse som matcher api respons fra historisk data
//data class LuftKvalitet (val id: String?, val zone: String?, val municipality: String?, val area: String?, val station: String?, val eoi: String?, val type: String?, val component: String?, val fromTime: String?, val toTime: String?, val value: Double?, val unit: String?, val latitude: Double?, val longitude: Double?, val timestep: Int?, val index: Int?, val color: String?, val isValid: Boolean?, val isVisible: Boolean?)