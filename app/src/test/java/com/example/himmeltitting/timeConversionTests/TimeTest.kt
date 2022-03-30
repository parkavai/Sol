package com.example.himmeltitting.timeConversionTests

import com.example.himmeltitting.utils.TimeConversion
import org.junit.Test

class TimeTest {

    private val timeconverter = TimeConversion()

    @Test
    fun currentTime_isNotNull() {
        val time = timeconverter.currentTime()
        print(time)
    }

    @Test
    fun convertTime_stripsEnd() {
        val time = timeconverter.sunTimeToForecastTime("2022-03-30T14:36:14+02:00")
        assert(time == "2022-03-30T14:36:14")
        print(time)
    }

    @Test
    fun timeStringToDate_returnsDate() {
        val date = timeconverter.timeStringToDate("2022-03-30T14:36:14+02:00")
        assert(date != null)
        print(date)
    }

    @Test
    fun subtractingDates() {
        val datea = timeconverter.timeStringToDate("2022-03-30T14:36:14+02:00")
        val dateb = timeconverter.timeStringToDate("2022-03-30T14:36:14")
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
        val string = timeconverter.prettyTimeString("2022-03-30T14:36:14")
        assert(string == "14:36:14")
    }

}