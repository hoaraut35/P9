package com.openclassrooms.realestatemanager.repositories


import com.openclassrooms.realestatemanager.database.RealEStateDao
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import com.openclassrooms.realestatemanager.models.RealEstatePOI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDatabaseRepository @Inject constructor(private val realEstateDao: RealEStateDao) {

    //work
    fun getFlowRealEstates() = realEstateDao.getFlowRealEstates()

    //work
    fun getFlowRealEstatesFull() = realEstateDao.getFlowRealEstatesFull()

    //work
    fun getFlowRealEstateFullById(myRealEstate: Int) = realEstateDao.getFlowRealEstateFullById(myRealEstate)

    //work with one champ?
    suspend fun insertRealEstateTest(realEstate: RealEstate) = realEstateDao.insertRealEstate(realEstate)

    suspend fun updateRealEstateTest(realEstate: RealEstate) = realEstateDao.updateRealEstateTest(realEstate)

    fun getRealEstate(id: Int) = realEstateDao.getLiveRealEstateById(id)

    fun getAllRealEstateWithMedias() = realEstateDao.getAllDataFromRealEstate()

    fun getLastRowId() = realEstateDao.getLastRowId()

    suspend fun insertRealEstate(realEstate: RealEstate) = realEstateDao.insert(realEstate)
    suspend fun insertRealEstatePhoto(realEstatePhoto: RealEstateMedia): Long =  realEstateDao.insertMedia(realEstatePhoto)
    suspend fun updateRealEstateMedia(realEstateMedia: RealEstateMedia) = realEstateDao.updateMedia(realEstateMedia)
    suspend fun insertRealEstatePOI(realEstatePOI: RealEstatePOI): Long =   realEstateDao.insertPointOfInteret(realEstatePOI)

    //update realEstate
    suspend fun updateRealEstate(realEstate: RealEstate) = realEstateDao.update(realEstate)

    suspend fun deleteMedia(media: RealEstateMedia) {  realEstateDao.deleteMedia(media)  }

}