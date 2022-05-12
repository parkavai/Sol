package com.example.soleklart.ui.bottomsheet

/**
 * OutputData data class.
 * Wrapper class containing all data as strings shown in application for location and date.
 */
data class OutputData(
    val header: String, val temperature: String, val cloudCover: String,
    val windSpeed: String, val precipitation6Hours: String,
    val airQuality: String
)