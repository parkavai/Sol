package com.example.himmeltitting

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson

class LocationforecastDS {
    private val gson = Gson()

    suspend fun getAllForecastData(lat: Double, lon: Double): LocationforecastBase {
        //complete?lat=-16.516667&lon=-68.166667&altitude=4150
        // ^eksempel med complete og altitude inkludert
        val path = "https://in2000-apiproxy.ifi.uio.no/weatherapi/locationforecast/2.0/compact?lat=${lat}&lon=${lon}"
        val response = fetchData(path)
        print(response)
        val data : LocationforecastBase = gson.fromJson(response, LocationforecastBase::class.java)

        return data

    }

    suspend fun getTimeseriesData(lat: Double, lon: Double) : List<Timeseries>? {
        val data = getAllForecastData(lat, lon)

        return data.properties?.timeseries
    }

    //henter data fra url med Fuel
    private suspend fun fetchData(url: String): String?{
        return try {
            // `$ curl --user-agent 'gruppenavn'`
            val request = Fuel.get(url).header(Headers.USER_AGENT, "Gruppe 4")
            //print(request.response())
            request.awaitString()
        }
        catch (exception: Exception) {
            println("A network request exception was thrown: ${exception.message}")
            null
        }
    }

}