package com.example.himmeltitting

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.himmeltitting.databinding.ActivityLocationTmpBinding

class LSMainActivitytmp : AppCompatActivity() {
    private lateinit var binding: ActivityLocationTmpBinding
    private val viewModel: LSViewModeltmp by viewModels()

    private val LAT = 60.10
    private val LON = 9.58

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationTmpBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun viewData(view: View) {
        //gjor om til data class
        lateinit var temperatureInstant: String
        lateinit var cloudCoverInstant: String
        lateinit var windSpeedInstant: String

        lateinit var precipitation6Hours: String
        lateinit var summary12Hours: String

        viewModel.getForecast(LAT, LON).observe(this) {
            //gjor om til opprett data class metode
            Log.d("LOCATIONFORECAST", it.toString())
            val timeseries = it.properties.timeseries
            val currentTimeseriesData = timeseries[0].data
            val currentTimeseriesInstantData = currentTimeseriesData.instant

            temperatureInstant = currentTimeseriesInstantData.details.air_temperature.toString()
            cloudCoverInstant = currentTimeseriesInstantData.details.cloud_area_fraction.toString()
            windSpeedInstant = currentTimeseriesInstantData.details.wind_speed.toString()

            precipitation6Hours = currentTimeseriesData.next_6_hours.details.precipitation_amount.toString()
            summary12Hours = currentTimeseriesData.next_12_hours.summary.symbol_code

            //gjor om til set text fra data class metode
            Log.d("TMP:", temperatureInstant)
            val textView = binding.textView
            val outText = "Nå:\n" +
                    "Temperatur: $temperatureInstant, Skydekke: $cloudCoverInstant, Vindhastighet: $windSpeedInstant\n" +
                    "Precipation neste 6 timene: $precipitation6Hours\n" +
                    "SymbolSummary neste 12 timer: $summary12Hours"

            textView.text = outText
        }
    }
}