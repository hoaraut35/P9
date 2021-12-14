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
    suspend fun updateRealEstateTest(realEstate: RealEstate) //suspend for use another thread

    @Update
    suspend fun update(realEstate: RealEstate) //suspend for use another thread

    @Delete
    suspend fun delete(realEstate: RealEstate) //suspend for use another thread

    @Transaction
    @Query("SELECT * FROM realEstate_table")
    fun getFlowRealEstatesFull() : Flow<List<RealEstateFull>>

    @Transaction
    @Query("SELECT * FROM realEstate_table WHERE realEstateId = :myRealEstate")
    fun getFlowRealEstateFullById(myRealEstate: Int): Flow<RealEstateFull>

    @Query("SELECT * FROM realEstate_table WHERE realEstateId = :id")
    fun getLiveRealEstateById(id: Int): LiveData<RealEstate>

    @Insert(onConflict = OnConflictStrategy.REPLACE) //replace if already exist
    suspend fun insertRealEstate(realEstate: RealEstate) : Long//suspend for use another thread


    @Transaction
    @Query("SELECT * FROM realEstate_table WHERE realEstateId")
    fun getAllDataFromRealEstate(): Flow<List<RealEstateWithMedia>>

    @Transaction
    @Query("SELECT * FROM realEstate_table WHERE realEstateId = :id")
    fun getCurrentRealEstateWithMedia(id: Int): Flow<RealEstateWithMedia>

    @Transaction
    @Query("SELECT * FROM realEstate_table WHERE realEstateId")
    fun getAllDataWithPOI(): Flow<RealEstateWithPOIs>

    @Insert(onConflict = OnConflictStrategy.REPLACE) //replace if already exist
    suspend fun insertMedia(realEstatePhoto: RealEstateMedia): Long //suspend for use another thread

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMedia(realEstateMedia: RealEstateMedia)

    @Insert(onConflict = OnConflictStrategy.REPLACE) //replace if already exist
    suspend fun insertPointOfInteret(realEstatePOI: RealEstatePOI): Long //suspend for use another thread

    @Query("SELECT MAX(realEstateId) + 1 FROM realEstate_table")
    fun getLastRowId(): Flow<Int>

    //used by CONTENT PROVIDER for testing
    @Query("SELECT * FROM realEstate_table")
    fun getRealEstateWithCursor(): Cursor

    @Delete
    suspend fun deleteMedia(media: RealEstateMedia)





    @RawQuery
    fun getRealEstateFiltered(query: SupportSQLiteQuery) : Flow<List<RealEstateFull>>

}