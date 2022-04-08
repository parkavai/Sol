package com.example.himmeltitting.utils


/**
 * Method that uses data from locationForecast API and calculates what image to represent
 */
fun cloudImageCalculator(cover : String): String {
    val cloudString = cover.trimEnd('%')
    val cloudFloat = cloudString.toFloat()
    if (cloudFloat < 10.0) {
        return "@drawable/sun"
    }
    else if (cloudFloat > 10.0 && cloudFloat < 80.0) {
        return "@drawable/cloudsun"
    }
    else if (cloudFloat > 80.0) {
        return "@drawable/clouds"
    }
    return ""
}

/**
 * Method that uses data from Nilu API and calculates what image to represent
 */
fun airQualityImageCalculator(qualityString : String): String {
    val quality = qualityString.toDoubleOrNull() ?: return "@drawable/factory_green"
    if (quality < 60.0) {
        return "@drawable/factory_green"
    }
    else if (quality > 60.0 && quality < 120.0) {
        return "@drawable/factory_orange"
    }
    else if (quality > 120.0) {
        return "@drawable/factory_red"
    }
    return ""
}