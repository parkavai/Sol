package com.example.soleklart.ds.nilu

import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

const val LAT = 59.9
const val LON = 10.7
const val TIME = "2022-04-01T23:36:14"
const val RADIUS = 20

class NiluDataSourceTest {

    private lateinit var source: NiluDataSource

    @Before
    fun setUp() {
        source = NiluDataSource()
    }

    @Test
    fun fetchNilu_isNotNull() {
        runBlocking {
            val result = source.fetchNilu(LAT, LON, RADIUS, TIME)
            print(result.toString())
            assert(result != null)
        }
    }
}