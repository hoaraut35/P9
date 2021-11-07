package com.openclassrooms.realestatemanager.repositories

import com.openclassrooms.realestatemanager.database.RealEStateDao
import com.openclassrooms.realestatemanager.models.RealEstate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDatabaseRepository @Inject constructor(private val realEstateDao: RealEStateDao) {

    val getAllRealEstate : List<RealEstate> get() = realEstateDao.getAllRealEstate()

}