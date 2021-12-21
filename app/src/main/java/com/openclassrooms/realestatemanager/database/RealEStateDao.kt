package com.openclassrooms.realestatemanager.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.models.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RealEStateDao {

    @Query("SELECT * FROM realEstate_table ")
    fun getFlowRealEstates(): Flow<List<RealEstate>>

    @Update
    suspend fun updateRealEstate(realEstate: RealEstate)

    @Transaction
    @Query("SELECT * FROM realEstate_table")
    fun getAllRealEstatesFull() : Flow<List<RealEstateFull>>

    @Transaction
    @Query("SELECT * FROM realEstate_table WHERE realEstateId = :myRealEstate")
    fun getFlowRealEstateFullById(myRealEstate: Int): Flow<RealEstateFull>

    @Query("SELECT * FROM realEstate_table WHERE realEstateId = :id")
    fun getLiveRealEstateById(id: Int): LiveData<RealEstate>

    @Insert(onConflict = OnConflictStrategy.REPLACE) //replace if already exist
    suspend fun insertRealEstate(realEstate: RealEstate) : Long//suspend for use another thread

    @Insert(onConflict = OnConflictStrategy.REPLACE) //replace if already exist
    suspend fun insertMedia(realEstatePhoto: RealEstateMedia): Long //suspend for use another thread

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMedia(realEstateMedia: RealEstateMedia)

    @Insert(onConflict = OnConflictStrategy.REPLACE) //replace if already exist
    suspend fun insertPOI(realEstatePOI: RealEstatePOI): Long //suspend for use another thread

    @Query("SELECT MAX(realEstateId) + 1 FROM realEstate_table")
    fun getLastRowId(): Flow<Int>

    @Query("SELECT MAX(photo_id) + 1 FROM RealEstateMedia")
    fun getLastRowIdForMedia(): Flow<Int>

    @Delete
    suspend fun deleteMedia(media: RealEstateMedia)

    //for content provider
    @Query("SELECT * FROM realEstate_table")
    fun getRealEstateWithCursor(): Cursor

    //for search fragment
    @RawQuery
    fun getRealEstateFiltered(query: SupportSQLiteQuery) : Flow<List<RealEstateFull>>

}