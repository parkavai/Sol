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

        /* Standard hent alt ut av NILU APIet
        viewModel.fetchNilu()

        viewModel.getNilu().observe(this){
            Log.d("Swag", it.toString())
        }

         */

        //Oppdaterer nilu = MutableLiveData<List<LuftKvalitet>>() i viewModel klassen til å kun inneholde luftkvalitetsobjekter innenfor en gitt radius
        viewModel.fetchNiluMedRadius(59.89864, 10.8149, 3) //Tilfeldige kordinater

        viewModel.getNilu().observe(this) {
            Log.d("radius", it.toString())
            Log.d("radiusStorrelse", it.size.toString())
            it.forEach {
                Log.d("STED", it.station.toString() + " " + it.value.toString())
                /* Eksempel her er:
                D/STED: Bryn skole 31.375543
                D/STED: E6 Alna senter 49.612002
                D/STED: Sofienbergparken 24.147626
                D/STED: Manglerud 52.230658
                 */
            }
        }

        /*
        Det finnes mange flere parametere man kan ha for å få ulike steder, istedenfor å implementere
        flere av disse, så er det bedre å ta de om vi eventuelt får bruk for dem.
        Eksempel på alt man kan få ut:
            "id": 38,
            "zone": "Stor-Oslo",
            "municipality": "Oslo",
            "area": "Oslo",
            "station": "Manglerud",
            "eoi": "NO0071A",
            "type": "Veinær stasjon",
            "component": "PM10",
            "fromTime": "2022-03-09T12:00:00+01:00",
            "toTime": "2022-03-09T13:00:00+01:00",
            "value": 50.7,
            "unit": "µg/m³",
            "latitude": 59.89864,
            "longitude": 10.8149,
            "timestep": 3600,
            "index": 1,
            "color": "6ee86e",
            "isValid": true,
            "isVisible": true
         */

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