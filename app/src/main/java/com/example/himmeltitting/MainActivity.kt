package com.example.himmeltitting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.himmeltitting.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.fetchLocation(40.7127,74.0059,"2022-03-08", 2, 25.00,  "05:00")
        viewModel.getLocation().observe(this){
            Log.d("Testing", "Day-1")
            Log.d("High-Moon ", it.time!![0].high_moon.toString())
            Log.d("Low-Moon ", it.time[0].low_moon.toString())
            Log.d("Moon-phase ", it.time[0].moonphase.toString())
            Log.d("Moon-position", it.time[0].moonposition.toString())
            Log.d("Moon-Rise", it.time[0].moonrise.toString())
            Log.d("Moon-shadow", it.time[0].moonshadow.toString())
            Log.d("Solar-midnight", it.time[0].solarmidnight.toString())
            Log.d("Solar-noon", it.time[0].solarnoon.toString())
            Log.d("Sun-Rise", it.time[0].sunrise.toString())
            Log.d("Sun-set", it.time[0].sunset.toString())

            // Hente data med noen dager fremover
            Log.d("Testing", "Day-2")
            Log.d("High-Moon2 ", it.time[1].high_moon.toString())
            Log.d("Low-Moon2 ", it.time[1].low_moon.toString())
            Log.d("Moon-phase2 ", it.time[1].moonphase.toString())
            Log.d("Moon-position2", it.time[1].moonposition.toString())
            Log.d("Moon-Rise2", it.time[1].moonrise.toString())
            Log.d("Moon-shadow2", it.time[1].moonshadow.toString())
            Log.d("Solar-midnight2", it.time[1].solarmidnight.toString())
            Log.d("Solar-noon2", it.time[1].solarnoon.toString())
            Log.d("Sun-Rise2", it.time[1].sunrise.toString())
            Log.d("Sun-set2", it.time[1].sunset.toString())
        }

    }
}