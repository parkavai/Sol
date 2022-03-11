package com.example.himmeltitting.nilu

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NiluDataSource {
    //Henter ut alt fra APIet
    suspend fun fetchNilus(): List<LuftKvalitet>? {
        val path = "https://api.nilu.no/aq/utd"
        val gson = Gson()

        try {
            // https://api.nilu.no/
            val liste = object : TypeToken<List<LuftKvalitet>>() {}.getType()
            val luftKvalitetList : List<LuftKvalitet> = gson.fromJson(Fuel.get(path).header(Headers.USER_AGENT, "Gruppe 4").awaitString(), liste)
            return luftKvalitetList
        } catch(exception: Exception) {
            Log.d("MAIN_ACTIVITY", "A network request exception was thrown: ${exception.message}")
            return null
        }
    }

    //Basert på kordinater + radius, så kan man finne stasjoner hvor det er målt luftkvalitet
    suspend fun fetchNilusMedRadius(latitude: Double?, longitude: Double?, radius: Int): List<LuftKvalitet>? {
        val path = "https://api.nilu.no/aq/utd"
        val parametere = "/$latitude/$longitude/$radius?method=within&components=no2"
        val gson = Gson()

        try {
            // https://api.nilu.no/
            val liste = object : TypeToken<List<LuftKvalitet>>() {}.getType()
            val luftKvalitetList : List<LuftKvalitet> = gson.fromJson(Fuel.get(path + parametere).header(Headers.USER_AGENT, "Gruppe 4").awaitString(), liste)
            return luftKvalitetList
        } catch(exception: Exception) {
            Log.d("MAIN_ACTIVITY", "A network request exception was thrown: ${exception.message}")
            return null
        }
    }
}

data class LuftKvalitet (val id: String?, val zone: String?, val municipality: String?, val area: String?, val station: String?, val eoi: String?, val type: String?, val component: String?, val fromTime: String?, val toTime: String?, val value: Double?, val unit: String?, val latitude: Double?, val longitude: Double?, val timestep: Int?, val index: Int?, val color: String?, val isValid: Boolean?, val isVisible: Boolean?)