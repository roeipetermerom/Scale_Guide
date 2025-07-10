package com.guyi.class25b_and_1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class LeaderboardViewModel : ViewModel() {
    private val _selectedLocation = MutableLiveData<LatLng>()
    val selectedLocation: LiveData<LatLng> get() = _selectedLocation

    fun selectLocation(location: LatLng) {
        _selectedLocation.value = location
    }
}