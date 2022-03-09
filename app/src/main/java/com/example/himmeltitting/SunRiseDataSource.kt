package com.example.himmeltitting

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson

class SunRise {

    // Et gson-objekt som hjelper med aa parse data fra JSON til Kotlin
    private val gson = Gson()
    dsadasdad
    // Ett eksempel paa API-kall
    private val path = "https://in2000-apiproxy.ifi.uio.no/weatherapi/sunrise/2.0/.json?lat=40.7127&lon=-74.0059&date=2022-03-08&offset=-05:00"

    // Returnerer "Location" siden den er viktigst aa plukke ut
    suspend fun fetchLocation(lat: String?, long: String?, dat: String?, offset: String?): Location? {
        try {
            val fetchObjekt = gson.fromJson(Fuel.get(path+"lat=${lat}&lon=${long}&date=${dat}&offset=${offset}").awaitString(), Base::class.java)
            return fetchObjekt.location
        } catch(exception: Exception) {
            println("A network request exception was thrown: ${exception.message}")
            return null
        }
    }
}

data class Base(val location: Location?, val meta: Meta?)

data class Location(val height: String?, val latitude: String?, val longitude: String?, val time: MutableList<Time>?)

data class Meta(val licenseurl: String?)

data class Time(val date: String?, val high_moon: High_moon?, val low_moon: Low_moon?, val moonphase: Moonphase?, val moonposition: Moonposition?, val moonrise: Moonrise?, val moonset: Moonset?, val moonshadow: Moonshadow?, val solarmidnight: Solarmidnight?, val solarnoon: Solarnoon?, val sunrise: Sunrise?, val sunset: Sunset?)

data class High_moon(val desc: String?, val elevation: String?, val time: String?)

data class Low_moon(val desc: String?, val elevation: String?, val time: String?)

data class Moonphase(val desc: String?, val time: String?, val value: String?)

data class Moonposition(val azimuth: String?, val desc: String?, val elevation: String?, val phase: String?, val range: String?, val time: String?)

data class Moonrise(val desc: String?, val time: String?)

data class Moonset(val desc: String?, val time: String?)

data class Moonshadow(val azimuth: String?, val desc: String?, val elevation: String?, val time: String?)

data class Solarmidnight(val desc: String?, val elevation: String?, val time: String?)

data class Solarnoon(val desc: String?, val elevation: String?, val time: String?)

data class Sunrise(val desc: String?, val time: String?)

data class Sunset(val desc: String?, val time: String?)
