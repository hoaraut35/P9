package com.openclassrooms.realestatemanager.repositories


import com.openclassrooms.realestatemanager.database.RealEStateDao
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import com.openclassrooms.realestatemanager.models.RealEstatePOI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDatabaseRepository @Inject constructor(private val realEstateDao: RealEStateDao) {

    fun getAllRealEstate() = realEstateDao.getAllRealEstate()

    //  fun getRealEstate(realEstateId : Int) = realEstateDao.getRealEstateWithId(realEstateId)
    fun getRealEstate(id: Int) = realEstateDao.getRealEstateWithId(id)

    fun getAllRealEstateWithPhotos() = realEstateDao.getAllDataFromRealEstate()

    fun getAllRealEstateWithPOI() = realEstateDao.getAllDataWithPOI()

    fun getLastRowId() = realEstateDao.getLastRowId()

    suspend fun insertRealEstate(realEstate: RealEstate) = realEstateDao.insert(realEstate)

    suspend fun insertRealEstatePhoto(realEstatePhoto: RealEstateMedia): Long =
        realEstateDao.insertPhoto(realEstatePhoto)

    suspend fun insertRealEstatePOI(realEstatePOI: RealEstatePOI): Long =
        realEstateDao.insertPointOfInteret(realEstatePOI)


    //update realEstate
    suspend fun updateRealEstate(realEstate: RealEstate) = realEstateDao.update(realEstate)


    //WARNING USED BY CONTENT PROVIDER
//    suspend fun getRealEstateWithCursor(realEstateId: Int) =
//        realEstateDao.getRealEstateWithCursor(realEstateId)

}