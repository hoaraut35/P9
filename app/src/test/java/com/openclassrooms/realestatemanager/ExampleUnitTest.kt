package com.openclassrooms.realestatemanager

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.openclassrooms.realestatemanager.database.RealEStateDao
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.lang.Exception
import kotlin.Throws

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {

    private lateinit var realEstateDao : RealEStateDao
    private lateinit var db : RealEstateDatabase

    @Before
    fun createDb(){
//        val context : Context = ApplicationProvider.getApplicationContext()

       // db = Room.inMemoryDatabaseBuilder(context, RealEstateDatabase::class.java)
         //   .allowMainThreadQueires()
           // .build()
    }


    @Test
    @Throws(Exception::class)
    fun addition_isCorrect() {
        Assert.assertEquals(4, (2 + 2).toLong())
    }
}