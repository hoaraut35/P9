package com.openclassrooms.realestatemanager.ui.map

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.api.ResponseGeocoding
import com.openclassrooms.realestatemanager.repositories.GeocodingRepository
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelMap @Inject constructor(
    private val localDatabaseRepository: LocalDatabaseRepository,
    private val localisationRepository: GeocodingRepository
) : ViewModel() {

    fun getRealEstateFull() = localDatabaseRepository.getFlowRealEstatesFull().asLiveData()

    var location: Location? = null
    var latLng: LatLng? = null

    fun setLocation(location: Location): LatLng {
        return LatLng(location.latitude, location.longitude)
    }

    //from ui to get data
    fun getResult(address: String) {
        localisationRepository.getLatLngAddress(address)
    }

    //retour for ui
    fun getLatLngFromRepo() : LiveData<List<ResponseGeocoding>>{
        return localisationRepository.getLatLngList()
    }

}