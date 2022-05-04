package com.example.soleklart.ds.sunrise

data class SunriseBase(val location: Location?, val meta: Meta?)

data class Location(
    val height: Double?,
    val latitude: Double?,
    val longitude: Double?,
    val time: MutableList<Time>?
)

data class Meta(val licenseurl: String?)

/*
* Merk at "Time"-klassen er en liste som består av alle de ulike objektene(Highmoon,Lowmoon osv)
* Denne vil bli mest brukt for å hente de dataene vi ønsker å bruke!!!
 */

data class Time(
    val date: String?,
    val high_moon: Highmoon?,
    val low_moon: Lowmoon?,
    val moonphase: Moonphase?,
    val moonposition: Moonposition?,
    val moonrise: Moonrise?,
    val moonset: Moonset?,
    val moonshadow: Moonshadow?,
    val solarmidnight: Solarmidnight?,
    val solarnoon: Solarnoon?,
    val sunrise: Sunrise?,
    val sunset: Sunset?
)

data class Highmoon(val desc: String?, val elevation: Double?, val time: String?)

data class Lowmoon(val desc: String?, val elevation: Double?, val time: String?)

data class Moonphase(val desc: String?, val time: String?, val value: Double?)

data class Moonposition(
    val azimuth: Double?,
    val desc: String?,
    val elevation: Double?,
    val phase: Double?,
    val range: Double?,
    val time: String?
)

data class Moonrise(val desc: String?, val time: String?)

data class Moonset(val desc: String?, val time: String?)

data class Moonshadow(
    val azimuth: Double?,
    val desc: String?,
    val elevation: Double?,
    val time: String?
)

data class Solarmidnight(val desc: String?, val elevation: String?, val time: String?)

data class Solarnoon(val desc: String?, val elevation: Double?, val time: String?)

data class Sunrise(val desc: String?, val time: String?)

data class Sunset(val desc: String?, val time: String?)

data class CompactSunriseData(val sunsetTime: String?, val sunriseTime: String?)
