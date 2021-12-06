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
class RoomTest {

    private lateinit var realEStateDao: RealEStateDao
    private lateinit var db: RealEstateDatabase
    private var realEstateTest: RealEstate? = null

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

        realEstateTest?.cityOfProduct = "Rennes"
        realEstateTest?.price = 1000

        realEstateTest?.let { realEStateDao.insert(it) }

        //realEStateDao.insert(RealEstate(cityOfProduct = "Rennes"))


        val size = realEStateDao.getFlowRealEstates().take(1)

        assertEquals(1, size.first().size)

    }


}

