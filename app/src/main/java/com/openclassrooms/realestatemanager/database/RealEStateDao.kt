package com.openclassrooms.realestatemanager.database

import androidx.room.*
import com.openclassrooms.realestatemanager.models.RealEstate
import kotlinx.coroutines.flow.Flow

@Dao //for room
interface RealEStateDao {

    @Query("SELECT * FROM realEstate_table")
    fun getAllRealEstate(): Flow<List<RealEstate>> //coroutines flow

    @Insert(onConflict = OnConflictStrategy.REPLACE) //replace if already exist
    suspend fun insert(realEstate: RealEstate) //suspend for use another thread

    @Update
    suspend fun update(realEstate: RealEstate) //suspend for use another thread

    @Delete
    suspend fun delete(realEstate: RealEstate) //suspend for use another thread
}