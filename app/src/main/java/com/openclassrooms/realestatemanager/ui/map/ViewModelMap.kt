package com.openclassrooms.realestatemanager.ui.map

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.api.ResponseGeocoding
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.repositories.GeocodingRepository
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelMap @Inject constructor(
    private val localDatabaseRepository: LocalDatabaseRepository,
    private val localisationRepository: GeocodingRepository
) : ViewModel() {

    var latLng: LatLng? = null
    var realEstate: RealEstate = RealEstate()

    fun getRealEstateFull() = localDatabaseRepository.getFlowRealEstatesFull().asLiveData()

    fun setLocation(location: Location): LatLng {
        return LatLng(location.latitude, location.longitude)
    }

    fun getLatLngForUI(address: String, id: Int) {
        localisationRepository.getLatLngAddress(address, id)
    }

    fun getLatLngForUI(): LiveData<ResponseGeocoding> {
        return localisationRepository.getLatLngList()
    }

    fun updateRealEstate(realEstate: RealEstate) {
        viewModelScope.launch { localDatabaseRepository.updateRealEstate(realEstate) }
    }

}