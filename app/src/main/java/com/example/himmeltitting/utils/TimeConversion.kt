package com.example.himmeltitting.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*



/**
 * Converts sunrise time of format yyyy-MM-dd'T'HH:mm:ssXXX,
 * to time in format yyyy-MM-dd'T'HH:mm:ss as String
 */
fun sunTimeToForecastTime(time: String): String {
    return time.split("+")[0]
}

/**
 * Returns current time in format yyyy-MM-dd'T'HH:mm:ss as String
 */
@SuppressLint("SimpleDateFormat")
fun currentTime(): String {
    val date = Calendar.getInstance().time
    //val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss") //or use getDateInstance()
    return formatter.format(date)
}

/**
 * Returns current date in format yyyy-MM-dd as String
 */
@SuppressLint("SimpleDateFormat")
fun currentDate(): String {
    val date = Calendar.getInstance().time
    //val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
    val formatter = SimpleDateFormat("yyyy-MM-dd") //or use getDateInstance()
    return formatter.format(date)
}

/**
 * Converts time string with format yyyy-MM-dd'T'HH:mm:ss
 * and returns string as Date class
 */
@SuppressLint("SimpleDateFormat")
fun timeStringToDate(date: String): Date? {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss") //or use getDateInstance()
    return formatter.parse(date)
}

/**
 * Converts time string with format yyyy-MM-dd'T'HH:mm:ssZXX
 * and returns string with format HH:mm:ss
 */
fun prettyTimeString(time: String): String {
    var timeString = time.split("T")[1].trimEnd('Z')
    if (timeString.contains('+')) {
        timeString = timeString.split("+")[0]
    }
    return timeString
}

/**
 * Returns current timezone offset from GMT with format +-HH:mm, example +02:00
 */
@SuppressLint("SimpleDateFormat")
fun timeZoneOffset(): String {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault())
    val timeZone = SimpleDateFormat("Z").format(calendar.time)
    return timeZone.substring(0, 3) + ":" + timeZone.substring(3, 5)
}

/**
 * Returns current date in format yyyy-MM-dd as String
 */
@SuppressLint("SimpleDateFormat")
fun yesterdaysDate(): String {

    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -1)
    val date = calendar.time

    //val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
    val formatter = SimpleDateFormat("yyyy-MM-dd") //or use getDateInstance()
    return formatter.format(date)
}