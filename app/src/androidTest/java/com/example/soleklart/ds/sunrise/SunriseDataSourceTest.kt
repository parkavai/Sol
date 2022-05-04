package com.example.soleklart.ds.sunrise

import com.example.soleklart.utils.currentDate
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

const val LAT = 59.9
const val LON = 10.7

class SunriseDataSourceTest {

    private lateinit var source: SunriseDataSource
    private lateinit var date : String

    @Before
    fun setUp() {
        source = SunriseDataSource()
        date = currentDate()
    }

    @Test
    fun getCompactSunriseData_isNotNull() {
        runBlocking {
            print(date)
            val result = source.getCompactSunriseData(LAT, LON, date)
            print(result.toString())
            assert(result != null)
        }
    }
}