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

        viewModel.fetchLocation(40.7127,74.0059,"2022-03-16", 4, 25.00,  "05:00")
        viewModel.getLocation().observe(this){
            var i = 0
            while(i < it.time!!.size){
                Log.d("Oversikt over", "Day ${i}" )
                Log.d("High-Moon ", it.time[i].high_moon.toString())
                Log.d("Low-Moon ", it.time[i].low_moon.toString())
                Log.d("Moon-phase ", it.time[i].moonphase.toString())
                Log.d("Moon-position", it.time[i].moonposition.toString())
                Log.d("Moon-Rise", it.time[i].moonrise.toString())
                Log.d("Moon-shadow", it.time[i].moonshadow.toString())
                Log.d("Solar-midnight", it.time[i].solarmidnight.toString())
                Log.d("Solar-noon", it.time[i].solarnoon.toString())
                Log.d("Sun-Rise", it.time[i].sunrise.toString())
                Log.d("Sun-set", it.time[i].sunset.toString())
                Log.d("\n", "")
                i += 1
            }
        }

    }
}