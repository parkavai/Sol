package com.example.himmeltitting.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class TimeConversion {


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

}