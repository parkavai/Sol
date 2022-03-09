package com.example.himmeltitting

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson

class SunRiseDataSource {

    // Et gson-objekt som hjelper med aa parse data fra JSON til Kotlin
    private val gson = Gson()

    // Ett eksempel paa API-kall
    private val path = "https://in2000-apiproxy.ifi.uio.no/weatherapi/sunrise/2.0/.json?"

    // Returnerer "Location" siden den er viktigst aa plukke ut
    suspend fun fetchLocation(lat: Double?, long: Double?, date: String?, days: Int?, height: Double?, offset: String?): Location? {
        try {
            // Forsikre at man ikke skriver over å se 15 dager fremover
            if(days!! <= 15){
                val parametere = "lat=${lat.toString()}&lon=${long.toString()}&date=${date}&days=${days}&height=${height}&offset=${offset}"
                val fetchObjekt = gson.fromJson(Fuel.get(path+parametere).awaitString(), Base::class.java)
                return fetchObjekt.location
            }
            else{
                println("Value of 'days' were over 15, must be 15 or under")
                return null
            }
        } catch(exception: Exception) {
            println("A network request exception was thrown: ${exception.message}")
            return null
        }
    }
}

data class Base(val location: Location?, val meta: Meta?)

data class Location(val height: Double?, val latitude: Double?, val longitude: Double?, val time: MutableList<Time>?)

data class Meta(val licenseurl: String?)

/*
* Merk at "Time"-klassen er en liste som består av alle de ulike objektene(Highmoon,Lowmoon osv)
* Denne vil bli mest brukt for å hente de dataene vi ønsker å bruke!!!
 */

data class Time(val date: String?, val high_moon: Highmoon?, val low_moon: Lowmoon?, val moonphase: Moonphase?, val moonposition: Moonposition?, val moonrise: Moonrise?, val moonset: Moonset?, val moonshadow: Moonshadow?, val solarmidnight: Solarmidnight?, val solarnoon: Solarnoon?, val sunrise: Sunrise?, val sunset: Sunset?)

data class Highmoon(val desc: String?, val elevation: Double?, val time: String?)

data class Lowmoon(val desc: String?, val elevation: Double?, val time: String?)

data class Moonphase(val desc: String?, val time: String?, val value: Double?)

data class Moonposition(val azimuth: Double?, val desc: String?, val elevation: Double?, val phase: Double?, val range: Double?, val time: String?)

data class Moonrise(val desc: String?, val time: String?)

data class Moonset(val desc: String?, val time: String?)

data class Moonshadow(val azimuth: Double?, val desc: String?, val elevation: Double?, val time: String?)

data class Solarmidnight(val desc: String?, val elevation: String?, val time: String?)

data class Solarnoon(val desc: String?, val elevation: Double?, val time: String?)

data class Sunrise(val desc: String?, val time: String?)

data class Sunset(val desc: String?, val time: String?)
