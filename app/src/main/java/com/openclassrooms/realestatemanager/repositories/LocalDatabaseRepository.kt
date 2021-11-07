package com.openclassrooms.realestatemanager.repositories

import com.openclassrooms.realestatemanager.database.RealEStateDao
import com.openclassrooms.realestatemanager.models.RealEstate
import javax.inject.Inject

class LocalDatabaseRepository @Inject constructor(private val realEstateDao: RealEStateDao) {

    val allRealEstate : List<RealEstate> get() = realEstateDao.getAllRealEstate()
}