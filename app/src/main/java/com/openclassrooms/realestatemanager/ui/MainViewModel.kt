package com.openclassrooms.realestatemanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


//get data from repository for ui survive configuration change

@HiltViewModel
class MainViewModel @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository ) : ViewModel() {

    var allRealEstate = localDatabaseRepository.getFlowRealEstates().asLiveData()

    //fir list fragment
    fun getRealEstateFull() = localDatabaseRepository.getFlowRealEstatesFull().asLiveData()

    var allRealEstateWithPhotos = localDatabaseRepository.getAllRealEstateWithMedias().asLiveData()

    //var getAllRealEstateWithVideos = localDatabaseRepository.getAllRealEstateWithVideos().asLiveData()

    var getLAstRowId = localDatabaseRepository.getLastRowId().asLiveData()

    fun insert(realEstate: RealEstate) = viewModelScope.launch {  localDatabaseRepository.insertRealEstate(realEstate) }

    fun insertPhoto(photo:RealEstateMedia) = viewModelScope.launch { localDatabaseRepository.insertRealEstatePhoto(photo) }

   // fun insertVideo(video:RealEstateVideo) = viewModelScope.launch { localDatabaseRepository.insertRealEstateVideo(video) }

    fun update(realEstate: RealEstate) = viewModelScope.launch{localDatabaseRepository.updateRealEstate(realEstate)}

}