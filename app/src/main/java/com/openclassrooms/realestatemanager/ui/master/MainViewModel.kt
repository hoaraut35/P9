package com.openclassrooms.realestatemanager.ui.master

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database.RealEStateDao
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.injection.Database
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import com.openclassrooms.realestatemanager.repositories.LocalisationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


//get data from repository for ui

@HiltViewModel
class MainViewModel @Inject constructor(private val localRepository : LocalDatabaseRepository) : ViewModel() {


  //  val allRealEstate : List<RealEstate> = localRepository.allRealEstate




    /*fun getAllRealEstate(): List<RealEstate>{
        return localRepository.getAllRealEstate
    }

     */





}