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

    suspend fun insertRealEstate(realEstate: RealEstate) : Long = realEstateDao.insertRealEstate(realEstate)
    suspend fun updateRealEstate(realEstate: RealEstate) = realEstateDao.update(realEstate)
    suspend fun deleteMedia(media: RealEstateMedia) {  realEstateDao.deleteMedia(media)  }
    suspend fun insertMedia(media: RealEstateMedia): Long =  realEstateDao.insertMedia(media)
    suspend fun insertPOI(poi: RealEstatePOI): Long =   realEstateDao.insertPOI(poi)

    fun getFlowRealEstateFullById(myRealEstate: Int) = realEstateDao.getFlowRealEstateFullById(myRealEstate)
    fun getFlowRealEstatesFull() = realEstateDao.getFlowRealEstatesFull()
    fun getLastRowIdForMedia() = realEstateDao.getLastRowIdForMedia()
    fun getRealEstateFiltered(query: SupportSQLiteQuery) = realEstateDao.getRealEstateFiltered(query)
    fun getLastRowId() = realEstateDao.getLastRowId()

}