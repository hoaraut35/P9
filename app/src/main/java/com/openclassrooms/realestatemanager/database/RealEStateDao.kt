package com.openclassrooms.realestatemanager.database

import androidx.room.*
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstatePhoto
import kotlinx.coroutines.flow.Flow

@Dao //for room
interface RealEStateDao {

    @Query("SELECT * FROM realEstate_table ")
    fun getAllRealEstate(): Flow<List<RealEstate>> //the data arrives as and when

   /* @Query(
        "SELECT * FROM realEstate_table " +
                "JOIN RealEstatePhoto ON realEstate_table.id = photos.id"
    )
    //+ "JOIN RealEstatePhoto ON realEstate_table.id = photos.id")
    fun getAllRealEstateWithPhotos(): Flow<Map<RealEstate, List<RealEstatePhoto>>>

    */


    @Insert(onConflict = OnConflictStrategy.REPLACE) //replace if already exist
    suspend fun insert(realEstate: RealEstate) //suspend for use another thread

    @Update
    suspend fun update(realEstate: RealEstate) //suspend for use another thread

    //we can't delete a property....
    @Delete
    suspend fun delete(realEstate: RealEstate) //suspend for use another thread


    //@Query("SELECT * FROM realEstate_table WHERE photo LIKE '%' ")
    //https://www.youtube.com/watch?v=sU-ot_Oz3AE&t=195s
}