package com.openclassrooms.realestatemanager.repositories


import com.openclassrooms.realestatemanager.database.RealEStateDao
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstatePhoto
import com.openclassrooms.realestatemanager.models.RealEstateWithPhotos
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDatabaseRepository @Inject constructor (private val realEstateDao: RealEStateDao) {

    //get
    fun allRealEstate() = realEstateDao.getAllRealEstate()

    //get alldata
    fun allRealEstateWithPhoto() = realEstateDao.getAllDataFromRealEstate()

    //insert
    suspend fun insertRealEstate(realEstate: RealEstate) = realEstateDao.insert(realEstate)

    suspend fun insertRealEstatePhoto(realEstatePhoto :RealEstatePhoto) = realEstateDao.insertPhoto(realEstatePhoto)



    //update
    suspend fun updateRealEstate(realEstate: RealEstate) = realEstateDao.update(realEstate)

    //delete not authorized

}