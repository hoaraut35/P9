package com.openclassrooms.realestatemanager.ui

import android.util.Log
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import com.openclassrooms.realestatemanager.repositories.SharedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository, private val sharedRepository: SharedRepository) :
    ViewModel() {

    private var mutableLiveDataRowId = MutableLiveData<Long>()

    fun insertRealEstate(realEstate: RealEstate) = viewModelScope.launch {
        val myResult = localDatabaseRepository.insertRealEstate(realEstate)
        mutableLiveDataRowId.value = myResult
        Log.i("[DAO]", "Id row added : $myResult")
    }

    //for UI
    fun observeRowId() : LiveData<Long>{
        return mutableLiveDataRowId
    }

    //**********************************************************************************************

    fun getRealEstateFull() = localDatabaseRepository.getFlowRealEstatesFull().asLiveData()

    var getLAstRowId = localDatabaseRepository.getLastRowId().asLiveData()

    fun insertPhoto(photo: RealEstateMedia) = viewModelScope.launch { localDatabaseRepository.insertRealEstatePhoto(photo) }

   // fun updateRealEstate(realEstate: RealEstate) = viewModelScope.launch { localDatabaseRepository.updateRealEstate(realEstate) }

    fun getResultListSearch() = sharedRepository.getResultListFromSearch()

    fun clearSearch() = sharedRepository.clearResult()



}