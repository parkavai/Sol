package com.example.himmeltitting.ds.nilu

import android.location.Location
import android.util.Log
import com.example.himmeltitting.utils.currentDate
import com.example.himmeltitting.utils.yesterdaysDate
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NiluDataSource {
    //Henter ut alt fra APIet
    suspend fun fetchNiluDefualt(): List<LuftKvalitet>? {
        val path = "https://api.nilu.no/aq/utd"
        val gson = Gson()

        return try {
            // https://api.nilu.no/
            val liste = object : TypeToken<List<LuftKvalitet>>() {}.type
            gson.fromJson(
                Fuel.get(path).header(Headers.USER_AGENT, "Gruppe 4").awaitString(), liste
            )
        } catch (exception: Exception) {
            Log.d("MAIN_ACTIVITY", "A network request exception was thrown: ${exception.message}")
            null
        }
    }

    //Basert på kordinater + radius, så kan man finne stasjoner hvor det er målt luftkvalitet
    suspend fun fetchNilu(latitude: Double, longitude: Double, radius: Int): LuftKvalitet? {
        val path = "https://api.nilu.no/aq/historical"
        val yesterday = yesterdaysDate()
        val today = currentDate()
        val parametere = "$yesterday/$today/$latitude/$longitude/$radius?method=within&components=no2"
        val gson = Gson()

        return try {
            // https://api.nilu.no/
            val liste = object : TypeToken<List<LuftKvalitet>>() {}.type
            val luftKvalitetList : List<LuftKvalitet> = gson.fromJson(Fuel.get(path + parametere).header(Headers.USER_AGENT, "Gruppe 4").awaitString(), liste)
            getClosestAirQuality(luftKvalitetList, latitude, longitude)
        } catch(exception: Exception) {
            Log.d("MAIN_ACTIVITY", "A network request exception was thrown: ${exception.message}")
            null
        }
    }

    /**
     * Returns Luftkvalitet Data class with closest Air Station for data
     */
    private fun getClosestAirQuality(list: List<LuftKvalitet>?, lat: Double, long: Double): LuftKvalitet? {
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
}

data class LuftKvalitet (val id: String?, val zone: String?, val municipality: String?, val area: String?, val station: String?, val eoi: String?, val type: String?, val component: String?, val fromTime: String?, val toTime: String?, val value: Double?, val unit: String?, val latitude: Double?, val longitude: Double?, val timestep: Int?, val index: Int?, val color: String?, val isValid: Boolean?, val isVisible: Boolean?)