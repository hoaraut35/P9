package com.openclassrooms.realestatemanager

import android.content.Context
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.database.RealEStateDao
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.models.RealEstate
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.properties.Delegates

@RunWith(AndroidJUnit4::class)
class RoomTest {

    private lateinit var realEStateDao: RealEStateDao
    private lateinit var db: RealEstateDatabase
    private var size : Int? = null

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
        size =  realEStateDao.getAllRealEstate().asLiveData().value?.size



    }.run {

        assertEquals(1, size)

    }


//assertEquals(2, listOfEstate.size)


}

