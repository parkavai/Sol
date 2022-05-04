package com.example.soleklart.ds.sunrise

import com.example.soleklart.utils.timeZoneOffset
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson

class SunriseDataSource {

    // Et gson-objekt som hjelper med aa parse data fra JSON til Kotlin
    private val gson = Gson()

    // Ett eksempel paa API-kall
    private val path = "https://in2000-apiproxy.ifi.uio.no/weatherapi/sunrise/2.0/.json?"

    // Returnerer "Location" siden den er viktigst aa plukke ut
    private suspend fun fetchLocation(lat: Double?, long: Double?, date: String?, days: Int?, height: Double?, offset: String?): Location? {
        return try {
            // Forsikre at man ikke skriver over å se 15 dager fremover
            if(days!! <= 15){
                val parametere = "lat=${lat}&lon=${long}&date=${date}&days=${days}&height=${height}&offset=${offset}"
                val fetchObjekt = gson.fromJson(Fuel.get(path+parametere).awaitString(), SunriseBase::class.java)
                fetchObjekt.location
            } else{
                println("Value of 'days' were over 15, must be 15 or under")
                null
            }
        } catch(exception: Exception) {
            println("A network request exception was thrown: ${exception.message}")
            null
        }
    }

    suspend fun getCompactSunriseData (latitude: Double, longitude: Double, date: String): CompactSunriseData? {
        val offset = timeZoneOffset() //utc time zone offset for norway is +01:00 winter time, +02:00 summer time
        val days = 1
        val height = 20.0
        val data = fetchLocation(latitude, longitude, date, days, height , offset)

        return data?.let { makeCompactSunriseData(it) }
    }

    private fun makeCompactSunriseData(data : Location): CompactSunriseData {
        val sunsetTime = data.time?.get(0)?.sunset?.time.toString()
        val sunriseTime = data.time?.get(0)?.sunrise?.time.toString()
        return CompactSunriseData(sunsetTime, sunriseTime)
    }
}
