package com.openclassrooms.realestatemanager

import android.content.ContentResolver
import android.content.ContentUris
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.database.RealEStateDao
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.provider.DatabaseContentProvider.Companion.myUriItem
import junit.framework.Assert.assertEquals
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestContentProvider {

    //this is a content provider to access room by another application
    private var myContentResolver: ContentResolver? = null

    private lateinit var realEStateDao: RealEStateDao
    private lateinit var db: RealEstateDatabase

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            RealEstateDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        realEStateDao = db.realEstateDao()
        myContentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver
    }

    //adjust the number of estate before testing
    @Test
    fun getRealEstate() {

        val cursor = myContentResolver!!.query(
            ContentUris.withAppendedId(myUriItem, 1),
            null,
            null,
            null,
            null
        )

        assertThat(cursor, Matchers.notNullValue())
        assertEquals(cursor!!.count, 4)
        cursor?.close()
    }

}