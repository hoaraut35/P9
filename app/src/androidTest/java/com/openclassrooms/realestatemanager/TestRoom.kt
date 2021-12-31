package com.openclassrooms.realestatemanager

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.database.RealEStateDao
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.models.RealEstate
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TestRoom {

    private lateinit var realEStateDao: RealEStateDao
    private lateinit var db: RealEstateDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, RealEstateDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        realEStateDao = db.realEstateDao()

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insert() = runBlocking {

        val realEstate = RealEstate(price = 100000)

        realEStateDao.insertRealEstate(realEstate)

        val listOfRealEstate = realEStateDao.getFlowRealEstates().take(1)
        assertEquals(1, listOfRealEstate.first().size)

    }

    @Test
    fun update() = runBlocking {
        val realEstate1 = RealEstate(realEstateId = 1, price = 100000)
        val realEstate2 = RealEstate(realEstateId = 2, price = 200000)

        realEStateDao.insertRealEstate(realEstate1)
        realEStateDao.insertRealEstate(realEstate2)

        realEStateDao.updateRealEstate(RealEstate(realEstateId = 1, price = 100001))
        realEStateDao.updateRealEstate(RealEstate(realEstateId = 2, price = 200002))

        val listOfRealEstate = realEStateDao.getFlowRealEstates().take(1)
        assertEquals(100001, listOfRealEstate.first()[0].price)
        assertEquals(200002, listOfRealEstate.first()[1].price)


    }

}

