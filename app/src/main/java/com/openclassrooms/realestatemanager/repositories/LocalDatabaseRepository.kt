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

    //ok 16/12/2021
    suspend fun updateRealEstate(realEstate: RealEstate) = realEstateDao.update(realEstate)

    //ok 16/12/2021
    fun getFlowRealEstatesFull() = realEstateDao.getFlowRealEstatesFull()

    //work
    fun getFlowRealEstateFullById(myRealEstate: Int) = realEstateDao.getFlowRealEstateFullById(myRealEstate)

    //work with one champ?
    suspend fun insertRealEstateTest(realEstate: RealEstate) = realEstateDao.insertRealEstate(realEstate)

    fun getLastRowId() = realEstateDao.getLastRowId()

    suspend fun insertRealEstate(realEstate: RealEstate) : Long = realEstateDao.insertRealEstate(realEstate)

    suspend fun insertRealEstateMedia(realEstatePhoto: RealEstateMedia): Long =  realEstateDao.insertMedia(realEstatePhoto)

    suspend fun insertRealEstatePOI(realEstatePOI: RealEstatePOI): Long =   realEstateDao.insertPOI(realEstatePOI)

    suspend fun deleteMedia(media: RealEstateMedia) {  realEstateDao.deleteMedia(media)  }

    //for search fragment
    fun getRealEstateFiltered(query: SupportSQLiteQuery) = realEstateDao.getRealEstateFiltered(query)

}