package com.openclassrooms.realestatemanager.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


//get data from repository for ui survive configuration change

@HiltViewModel
class MainViewModel @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository ) : ViewModel() {

    //get all realestate
    var allRealEstate = localDatabaseRepository.allRealEstate().asLiveData()

    //get all estate with photo
    var allRealEstateWithPhotos = localDatabaseRepository.allRealEstateWithPhoto().asLiveData()


    //insert
    fun insert(realEstate: RealEstate) = viewModelScope.launch {  localDatabaseRepository.insertRealEstate(realEstate) }

    //update
    fun update(realEstate: RealEstate) = viewModelScope.launch{localDatabaseRepository.updateRealEstate(realEstate)}

    //delete not authorized

}