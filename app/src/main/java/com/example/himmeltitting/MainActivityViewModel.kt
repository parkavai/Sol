package com.example.himmeltitting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel: ViewModel() {

    private val dataSource = SunRiseDataSource()
    private val location = MutableLiveData<Location>()

    fun getLocation(): MutableLiveData<Location> {
        return location
    }

    fun fetchLocation(lat: Double?, long: Double?, date: String?, days: Int?, height: Double?, offset: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            dataSource.fetchLocation(lat, long, date, days, height, offset).also{
                location.postValue(it)
            }
        }
    }
}