package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.database.RealEStateDao
import com.openclassrooms.realestatemanager.models.RealEstate


import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDatabaseRepository @Inject constructor(private val realEstateDao: RealEStateDao) {

    val allRealEstate : List<RealEstate> = realEstateDao.getAllRealEstate()

   // fun getAllRealEstateFlow() : LiveData<List<RealEstate>> = realEstateDao.getAllRealEstate()
}