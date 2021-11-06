package com.openclassrooms.realestatemanager.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.openclassrooms.realestatemanager.models.RealEstate

@Dao
interface RealEStateDao {

    @Query("SELECT * FROM RealEstate")
    fun getAllRealEstate() : List<RealEstate>

    @Delete
    fun delete(realEstate: RealEstate)

}