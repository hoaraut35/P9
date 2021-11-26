package com.openclassrooms.realestatemanager.repositories


import com.openclassrooms.realestatemanager.database.RealEStateDao
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstatePhoto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDatabaseRepository @Inject constructor (private val realEstateDao: RealEStateDao) {

    fun getAllRealEstate() = realEstateDao.getAllRealEstate()

    fun getAllRealEstateWithPhotos() = realEstateDao.getAllDataFromRealEstate()

    //fun getAllRealEstateWithVideos() = realEstateDao.getAllVideosForRealEstate()

    fun getLastRowId() = realEstateDao.getLastRowId()

    suspend fun insertRealEstate(realEstate: RealEstate) = realEstateDao.insert(realEstate)

    suspend fun insertRealEstatePhoto(realEstatePhoto :RealEstatePhoto) : Long  = realEstateDao.insertPhoto(realEstatePhoto)

   // suspend fun insertRealEstateVideo(realEstateVideo: RealEstateVideo) : Long  = realEstateDao.insertVideo(realEstateVideo)

    suspend fun updateRealEstate(realEstate: RealEstate) = realEstateDao.update(realEstate)

}