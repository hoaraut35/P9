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
    suspend fun updateRealEstate(realEstate: RealEstate) = realEstateDao.updateRealEstate(realEstate)

    suspend fun insertMedia(media: RealEstateMedia): Long = realEstateDao.insertMedia(media)
    suspend fun deleteMedia(media: RealEstateMedia) { realEstateDao.deleteMedia(media)  }

    suspend fun insertPOI(poi: RealEstatePOI): Long = realEstateDao.insertPOI(poi)

    fun getRealEstateFullById(myRealEstate: Int) = realEstateDao.getFlowRealEstateFullById(myRealEstate)
    fun getRealEstatesFullList() = realEstateDao.getAllRealEstatesFull()
    fun getRealEstatesFullListFiltered(query: SupportSQLiteQuery) = realEstateDao.getRealEstateFiltered(query)

    fun getLastRowIdForRealEstate() = realEstateDao.getLastRowId()

}