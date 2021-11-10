package com.openclassrooms.realestatemanager.database

import androidx.room.*
import com.openclassrooms.realestatemanager.models.RealEstate
import kotlinx.coroutines.flow.Flow

//method to access database

@Dao
interface RealEStateDao {

    @Query("SELECT * FROM RealEstate")
    fun getAllRealEstate(): Flow<List<RealEstate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(realEstate: RealEstate) //suspend for use another thread

    @Update
    suspend fun update(realEstate: RealEstate) //suspend for use another thread

    @Delete
    suspend fun delete(realEstate: RealEstate) //suspend for use another thread
}