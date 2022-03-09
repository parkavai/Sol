package com.example.himmeltitting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel: ViewModel() {

    private val dataSource = NiluDataSource()

    private val nilu = MutableLiveData<List<LuftKvalitet>>()

    fun getNilu(): LiveData<List<LuftKvalitet>> {
        return nilu
    }

    fun fetchNilu() {
        viewModelScope.launch(Dispatchers.IO) {
            dataSource.fetchNilus().also {
                nilu.postValue(it)
            }
        }
    }

    //Radius er nødt til å være int, altså et heltall siden det er slik APIet fungerer
    fun fetchNiluMedRadius(latitude: Double?, longitude: Double?, radius: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataSource.fetchNilusMedRadius(latitude, longitude, radius).also {
                nilu.postValue(it)
            }
        }
    }

}