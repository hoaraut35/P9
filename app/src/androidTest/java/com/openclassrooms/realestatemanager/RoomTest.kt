package com.openclassrooms.realestatemanager

import android.content.Context
import androidx.compose.ui.input.key.Key.Companion.Sleep
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import com.openclassrooms.realestatemanager.database.RealEStateDao
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.models.RealEstate
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoomTest {

    private lateinit var realEStateDao: RealEStateDao
    private lateinit var db: RealEstateDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, RealEstateDatabase::class.java)
            .allowMainThreadQueries().build()
        realEStateDao = db.realEstateDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun write() = runBlocking {
        val realEstate1 = realEStateDao.insert(RealEstate(cityOfProduct = "Rennes"))
        val realEstate2 = realEStateDao.insert(RealEstate(cityOfProduct = "Rennes"))
        val realEstate3 = realEStateDao.insert(RealEstate(cityOfProduct = "Rennes"))
        val listOfEstate = realEStateDao.getAllDataFromRealEstate().asLiveData().getOrAwaitValue {

        }
        Assert.assertEquals(3, listOfEstate)
    }


}