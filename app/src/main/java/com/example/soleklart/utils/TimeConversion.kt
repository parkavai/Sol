package com.example.soleklart.utils

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
 * Returns yesterdays date in format yyyy-MM-dd as String
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

/**
 * Returns time string + 2 hours in format yyyy-MM-dd'T'HH:mm:ss as String
 */
@SuppressLint("SimpleDateFormat")
fun plusHours(time: String, hours: Int): String? {
    val calendar = Calendar.getInstance()
    val date = timeStringToDate(time)
    calendar.time = date ?: return null
    calendar.add(Calendar.HOUR_OF_DAY, hours)
    val newDate = calendar.time

    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss") //or use getDateInstance()
    return formatter.format(newDate)
}


/**
 * returns header based on time type (sunrise, sunset, after)
 */
fun timeTypeToHeader(type: String): String {
    return when (type) {
        "sunrise" -> "Soloppgang:"
        "sunset" -> "Solnedgang:"
        "after" -> "2 timer etter solnedgang:"
        else -> "Invalid type"
    }
}

/**
 * Returns Calendar with current date + days
 */
fun getCalendarDaysFromToday(days: Int): Calendar {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_MONTH, days)
    return calendar
}