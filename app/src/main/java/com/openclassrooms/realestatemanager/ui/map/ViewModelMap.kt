package com.openclassrooms.realestatemanager.ui.map

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.api.ResponseGeocoding
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateFull
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

    //to persist data...
    var latLng: LatLng? = null
    var realEstate: RealEstate = RealEstate()

    //convert location to LatLng for UI...
    fun getLocationToLatLng(location: Location): LatLng {
        return LatLng(location.latitude, location.longitude)
    }

    //to observe data from UI...
    fun getRealEstateFullList() : LiveData<List<RealEstateFull>> {
        return localDatabaseRepository.getRealEstatesFullList().asLiveData()
    }

    //send query to repository from UI...
    fun getLatLngAddressForUI(address: String, realEstateId: Int) {
        localisationRepository.getLatLngAddress(address, realEstateId)
    }

    //to observe data from UI...
    fun getLatLngAddressForUI(): LiveData<ResponseGeocoding> {
        return localisationRepository.getLatLngLiveData()
    }

    //send update query to repository...
    fun updateRealEstate(realEstate: RealEstate) {
        viewModelScope.launch { localDatabaseRepository.updateRealEstate(realEstate) }
    }

}