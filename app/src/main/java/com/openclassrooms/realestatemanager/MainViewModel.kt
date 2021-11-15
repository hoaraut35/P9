package com.openclassrooms.realestatemanager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


//get data from repository for ui survive configuration change

@HiltViewModel
class MainViewModel @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository ) : ViewModel() {



    var allRealEstate = localDatabaseRepository.allRealEstate().asLiveData()

   // fun insert(realEstate: RealEstate) = viewModelScope.launch { localDatabaseRepository.insert(realEstate)}


    fun updateRealEstateChoice(choice : Int){
        Log.i("[THOMAS]", "Selected property id : $choice")
    }
}