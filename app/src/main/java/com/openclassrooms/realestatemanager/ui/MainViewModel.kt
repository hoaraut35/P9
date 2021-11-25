package com.openclassrooms.realestatemanager.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstatePhoto
import com.openclassrooms.realestatemanager.models.RealEstateWithPhotos
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


//get data from repository for ui survive configuration change

@HiltViewModel
class MainViewModel @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository ) : ViewModel() {

    var allRealEstate = localDatabaseRepository.allRealEstate().asLiveData()

    var allRealEstateWithPhotos = localDatabaseRepository.allRealEstateWithPhoto().asLiveData()

    var getLAstRowId = localDatabaseRepository.getLastRowId().asLiveData()

    fun insert(realEstate: RealEstate) = viewModelScope.launch {  localDatabaseRepository.insertRealEstate(realEstate) }

    fun insertPhoto(photo:RealEstatePhoto) = viewModelScope.launch { localDatabaseRepository.insertRealEstatePhoto(photo) }

    fun update(realEstate: RealEstate) = viewModelScope.launch{localDatabaseRepository.updateRealEstate(realEstate)}

}