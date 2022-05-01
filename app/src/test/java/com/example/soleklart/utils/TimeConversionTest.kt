package com.example.soleklart.utils

import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

const val TIMESTAMP = "2022-03-30T14:36:14+02:00"

class TimeConversionTest {

    @Before
    fun setUp() {
    }

    @Test
    fun convertTime_stripsEnd() {
        val time = sunTimeToForecastTime(TIMESTAMP)
        assert(time == "2022-03-30T14:36:14")
        print(time)
    }

    @Test
    fun timeStringToDate_returnsDate() {
        val date = timeStringToDate(TIMESTAMP)
        assert(date != null)
        print(date)
    }

    @Test
    fun subtractingDates() {
        val datea = timeStringToDate(TIMESTAMP)
        val dateb = timeStringToDate("2022-03-30T14:36:14")
        assert(datea != null)
        assert(dateb != null)
        if (datea != null) {
            print(datea.time.toString() + "\n")
        }
        if (dateb != null) {
            print(dateb.time.toString()+ "\n")
        }
        if (datea != null) {
            if (dateb != null) {
                assert(datea.time == dateb.time)
            }
        }
    }

    @Test
    fun prettyStringTest() {
        val string = prettyTimeString(TIMESTAMP)
        assert(string == "14:36:14")
    }

    @Test
    fun sunTimeToForecastTime() {
    }

    @Test
    fun currentTime() {
    }

    @Test
    fun currentDate() {
    }

    @Test
    fun timeStringToDate() {
    }

    @Test
    fun prettyTimeString() {
    }

    @Test
    fun timeZoneOffset() {
    }

    @Test
    fun yesterdaysDate() {
    }

    @Test
    fun plusHours() {
    }

    @Test
    fun timeTypeToHeader() {
    }

    @Test
    fun getCalendarDaysFromToday() {
    }
}