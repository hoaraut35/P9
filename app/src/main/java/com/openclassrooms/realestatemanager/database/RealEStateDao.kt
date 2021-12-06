package com.openclassrooms.realestatemanager.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.models.*
import kotlinx.coroutines.flow.Flow

@Dao //for room
interface RealEStateDao {

    //work
    @Query("SELECT * FROM realEstate_table ")
    fun getFlowRealEstates(): Flow<List<RealEstate>>

    //work
    @Insert(onConflict = OnConflictStrategy.REPLACE) //replace if already exist
    suspend fun insertRealEstate(realEstate: RealEstate) //suspend for use another thread

    //?
    @Update
    suspend fun updateRealEstateTest(realEstate: RealEstate) //suspend for use another thread




    //work
    @Transaction
    @Query("SELECT * FROM realEstate_table")
    fun getFlowRealEstatesFull() : Flow<List<RealEstateFull>>

    //work
    @Transaction
    @Query("SELECT * FROM realEstate_table WHERE realEstateId = :myRealEstate")
    fun getFlowRealEstateFullById(myRealEstate: Int): Flow<RealEstateFull>










    //used by update framgent
    @Query("SELECT * FROM realEstate_table WHERE realEstateId = :id")
    fun getLiveRealEstateById(id: Int): LiveData<RealEstate>




    //insert realstate
    @Insert(onConflict = OnConflictStrategy.REPLACE) //replace if already exist
    suspend fun insert(realEstate: RealEstate) //suspend for use another thread


    //update realstate
    @Update
    suspend fun update(realEstate: RealEstate) //suspend for use another thread


    //we can't delete a property....
    @Delete
    suspend fun delete(realEstate: RealEstate) //suspend for use another thread




    @Transaction
    @Query("SELECT * FROM realEstate_table WHERE realEstateId")
    fun getAllDataFromRealEstate(): Flow<List<RealEstateWithMedia>>

    @Transaction
    @Query("SELECT * FROM realEstate_table WHERE realEstateId = :id")
    fun getCurrentRealEstateWithMedia(id: Int): Flow<RealEstateWithMedia>

    @Transaction
    @Query("SELECT * FROM realEstate_table WHERE realEstateId")
    fun getAllDataWithPOI(): Flow<RealEstateWithPOIs>

//    @Transaction
//    @Query("SELECT * FROM realEstate_table WHERE realEstateId = :id" )
//    fun getCurrentRealEstateWithPOI(id : Int): Flow<RealEstateWithPOIs>




    //insert photo
    @Insert(onConflict = OnConflictStrategy.REPLACE) //replace if already exist
    suspend fun insertPhoto(realEstatePhoto: RealEstateMedia): Long //suspend for use another thread

    //insert point of interet
    @Insert(onConflict = OnConflictStrategy.REPLACE) //replace if already exist
    suspend fun insertPointOfInteret(realEstatePOI: RealEstatePOI): Long //suspend for use another thread





    @Query("SELECT MAX(realEstateId) + 1 FROM realEstate_table")
    fun getLastRowId(): Flow<Int>


    //used by CONTENT PROVIDERcontent provider for testing
    @Query("SELECT * FROM realEstate_table WHERE realEstateId = :realEstateId")
    fun getRealEstateWithCursor(realEstateId: Int): Cursor




    //******************** QUERY MULTITABLE *********************************
  //  @Query("SELECT * FROM realEstate_table " + "INNER JOIN ")





}