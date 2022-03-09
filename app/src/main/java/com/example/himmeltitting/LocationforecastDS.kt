package com.example.himmeltitting

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson

class LocationforecastDS {
    private val gson = Gson()

    suspend fun getData(lat: Double, lon: Double): LocationforecastBase {
        val path = "https://in2000-apiproxy.ifi.uio.no/weatherapi/locationforecast/2.0/compact?lat=${lat}&lon=${lon}"
        // `$ curl --user-agent 'gruppenavn'`
        val response = fetchData(path)
        val data : LocationforecastBase = gson.fromJson(response, LocationforecastBase::class.java)

        return data

    }

    //henter data fra url med Fuel
    private suspend fun fetchData(url: String): String?{
        return try {
            Fuel.get(url).awaitString()
        }
        catch (exception: Exception) {
            println("A network request exception was thrown: ${exception.message}")
            null
        }
    }

}