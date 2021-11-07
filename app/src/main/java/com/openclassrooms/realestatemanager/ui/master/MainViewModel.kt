package com.openclassrooms.realestatemanager.ui.master

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database.RealEStateDao
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.injection.Database
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import com.openclassrooms.realestatemanager.repositories.LocalisationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val localRepository : LocalDatabaseRepository) : ViewModel() {



    //val

    fun addNewRealEstate(){

    }
}