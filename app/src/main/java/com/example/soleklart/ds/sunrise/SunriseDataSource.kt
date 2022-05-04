package com.example.soleklart.ds.sunrise

import com.example.soleklart.utils.timeZoneOffset
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson

/**
 * A class which has the responsibility for the data source over the sunrise-api
 */
class SunriseDataSource {
    private val gson = Gson()

    private val path = "https://in2000-apiproxy.ifi.uio.no/weatherapi/sunrise/2.0/.json?"

    /**
     * Calls on the sunrise-api and initialises Location-object
     */
    private suspend fun fetchLocation(
        lat: Double?,
        long: Double?,
        date: String?,
        days: Int?,
        height: Double?,
        offset: String?
    ): Location? {
        return try {
            if (days!! <= 15) {
                val parameters =
                    "lat=${lat}&lon=${long}&date=${date}&days=${days}&height=${height}&offset=${offset}"
                val fetchObject = gson.fromJson(
                    Fuel.get(path + parameters).awaitString(),
                    SunriseBase::class.java
                )
                fetchObject.location
            } else {
                println("Value of 'days' were over 15, must be 15 or under")
                null
            }
        } catch (exception: Exception) {
            println("A network request exception was thrown: ${exception.message}")
            null
        }
    }

    /**
     * Returns only the necessary data from the sunrise-api which is used for the application
     */
    suspend fun getCompactSunriseData(
        latitude: Double,
        longitude: Double,
        date: String
    ): CompactSunriseData? {
        val offset =
            timeZoneOffset() //utc time zone offset for norway is +01:00 winter time, +02:00 summer time
        val days = 1
        val height = 20.0
        val data = fetchLocation(latitude, longitude, date, days, height, offset)

        return data?.let { makeCompactSunriseData(it) }
    }

    /**
     * Creates the dataholder with the necessary data from the sunrise-api. These are
     * the sunset and sunrise time.
     */
    private fun makeCompactSunriseData(data: Location): CompactSunriseData {
        val sunsetTime = data.time?.get(0)?.sunset?.time.toString()
        val sunriseTime = data.time?.get(0)?.sunrise?.time.toString()
        return CompactSunriseData(sunsetTime, sunriseTime)
    }
}
