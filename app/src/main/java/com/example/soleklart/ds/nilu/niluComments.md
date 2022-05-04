Standard get all from NILU API:
        viewModel.fetchNilu()

        viewModel.getNilu().observe(this){
            Log.d("Swag", it.toString())
        }


// Update nilu = MutableLiveData<List<LuftKvalitet>>() i viewModel klassen til å kun inneholde luftkvalitetsobjekter innenfor en gitt radius
viewModel.fetchNiluMedRadius(59.89864, 10.8149, 3) //Tilfeldige kordinater

viewModel.getNilu().observe(this) {
    it.forEach {
        Log.d("STED", it.station.toString() + " " + it.value.toString())
        /* Example is:
        D/STED: Bryn skole 31.375543
        D/STED: E6 Alna senter 49.612002
        D/STED: Sofienbergparken 24.147626
        D/STED: Manglerud 52.230658
         */
    }
}

All data available data:
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