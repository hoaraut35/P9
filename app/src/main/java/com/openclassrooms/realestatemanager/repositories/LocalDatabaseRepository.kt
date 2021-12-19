package com.openclassrooms.realestatemanager.repositories


import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.database.RealEStateDao
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import com.openclassrooms.realestatemanager.models.RealEstatePOI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDatabaseRepository @Inject constructor(private val realEstateDao: RealEStateDao) {

    suspend fun updateRealEstate(realEstate: RealEstate) = realEstateDao.update(realEstate)
    suspend fun insertRealEstateTest(realEstate: RealEstate) = realEstateDao.insertRealEstate(realEstate)
    suspend fun insertRealEstate(realEstate: RealEstate) : Long = realEstateDao.insertRealEstate(realEstate)
    suspend fun insertRealEstateMedia(realEstatePhoto: RealEstateMedia): Long =  realEstateDao.insertMedia(realEstatePhoto)
    suspend fun insertRealEstatePOI(realEstatePOI: RealEstatePOI): Long =   realEstateDao.insertPOI(realEstatePOI)
    suspend fun deleteMedia(media: RealEstateMedia) {  realEstateDao.deleteMedia(media)  }

    fun getFlowRealEstatesFull() = realEstateDao.getFlowRealEstatesFull()
    fun getFlowRealEstateFullById(myRealEstate: Int) = realEstateDao.getFlowRealEstateFullById(myRealEstate)
    fun getLastRowId() = realEstateDao.getLastRowId()
    fun getLastRowIdForMedia() = realEstateDao.getLastRowIdForMedia()
    fun getRealEstateFiltered(query: SupportSQLiteQuery) = realEstateDao.getRealEstateFiltered(query)

}