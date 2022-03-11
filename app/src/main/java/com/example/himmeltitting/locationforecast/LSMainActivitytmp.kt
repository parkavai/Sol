package com.example.himmeltitting.locationforecast

import android.os.Bundle
import android.view.View
import android.widget.TextView
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
        viewModel.getCompactForecast(LAT, LON).observe(this) {
            setForecastText(binding.textView, it)
        }
    }

    private fun setForecastText(textView: TextView, data: CompactTimeSeriesData?) {
        if (data == null){
            textView.text = "Kunne ikke hente data"
            return
        }

        val outText = "Nå (${data.time}):\n" +
                "Temperatur: ${data.temperature}, Skydekke: ${data.cloudCover}, Vindhastighet: ${data.wind_speed}\n" +
                "Precipation neste 6 timene: ${data.precipitation6Hours}\n" +
                "SymbolSummary neste 12 timer: ${data.summary12Hour}"

        textView.text = outText
    }
}