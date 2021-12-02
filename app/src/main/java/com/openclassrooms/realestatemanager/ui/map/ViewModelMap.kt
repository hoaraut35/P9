package com.openclassrooms.realestatemanager.ui.map

import android.location.Location
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelMap @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository): ViewModel() {

    var location : Location? = null
    var latLng : LatLng? = null

    fun setLocation(location: Location):LatLng {

        return LatLng(location.latitude, location.longitude)
    }

}