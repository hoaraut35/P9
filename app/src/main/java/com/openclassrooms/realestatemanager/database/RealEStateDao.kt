package com.openclassrooms.realestatemanager.database

import androidx.room.*
import com.openclassrooms.realestatemanager.models.*
import kotlinx.coroutines.flow.Flow

@Dao //for room
interface RealEStateDao {

    //get all realerstate
    @Query("SELECT * FROM realEstate_table ")
    fun getAllRealEstate(): Flow<List<RealEstate>> //the data arrives as and when

    //insert realstate
    @Insert(onConflict = OnConflictStrategy.REPLACE) //replace if already exist
    suspend fun insert(realEstate: RealEstate) //suspend for use another thread

    //update realstate
    @Update
    suspend fun update(realEstate: RealEstate) //suspend for use another thread

    //we can't delete a property....
    @Delete
    suspend fun delete(realEstate: RealEstate) //suspend for use another thread

    //get all realestate with jointure on photos
    @Transaction
    @Query("SELECT * FROM realEstate_table WHERE realEstateId")
    fun getAllDataFromRealEstate(): Flow<List<RealEstateWithMedia>>
    //insert photo
    @Insert(onConflict = OnConflictStrategy.REPLACE) //replace if already exist
    suspend fun insertPhoto(realEstatePhoto: RealEstateMedia) : Long //suspend for use another thread










    @Query("SELECT MAX(realEstateId) + 1 FROM realEstate_table")
    fun getLastRowId(): Flow<Int>



    //@Query("SELECT * FROM realEstate_table WHERE photo LIKE '%' ")
    //https://www.youtube.com/watch?v=sU-ot_Oz3AE&t=195s
}