package com.example.himmeltitting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.himmeltitting.nilu.*
import com.example.himmeltitting.sunrise.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel: ViewModel() {

    private val sunriseDS = SunRiseDataSource()
    private val niluDS = NiluDataSource()

    private val location = MutableLiveData<Location>()

    fun getLocation(): MutableLiveData<Location> {
        return location
    }

    fun fetchLocation(lat: Double?, long: Double?, date: String?, days: Int?, height: Double?, offset: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            sunriseDS.fetchLocation(lat, long, date, days, height, offset).also{
                location.postValue(it)
            }
        }
    }

    private val nilu = MutableLiveData<List<LuftKvalitet>>()

    fun getNilu(): LiveData<List<LuftKvalitet>> {
        return nilu
    }

    fun fetchNilu() {
        viewModelScope.launch(Dispatchers.IO) {
            niluDS.fetchNilus().also {
                nilu.postValue(it)
            }
        }
    }

    //Radius er nødt til å være int, altså et heltall siden det er slik APIet fungerer
    fun fetchNiluMedRadius(latitude: Double?, longitude: Double?, radius: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            niluDS.fetchNilusMedRadius(latitude, longitude, radius).also {
                nilu.postValue(it)
            }
        }
    }

}