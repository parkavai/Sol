package com.example.soleklart.ds.sunrise

import com.example.soleklart.utils.currentDate
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

const val LAT = 59.9
const val LON = 10.7

class SunRiseDataSourceTest {

    private lateinit var source: SunRiseDataSource
    private lateinit var date : String

    @Before
    fun setUp() {
        source = SunRiseDataSource()
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