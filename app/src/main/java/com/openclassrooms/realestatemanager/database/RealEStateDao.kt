package com.openclassrooms.realestatemanager.database

import androidx.room.*
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateAgent

@Dao
interface RealEStateDao {

    @Query("SELECT * FROM RealEstate")
    //fun getAllRealEstate() : List<RealEstate>
    fun getAllRealEstate() : List<RealEstate>

    @Query("SELECT * FROM RealEstateAgent")
    fun getAllRealEstateAgent() : List<RealEstateAgent>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(realEstate : List<RealEstate>)

    @Delete
    suspend fun delete(realEstate: RealEstate)

    @Update
    suspend fun update(realEstate: RealEstate)

}