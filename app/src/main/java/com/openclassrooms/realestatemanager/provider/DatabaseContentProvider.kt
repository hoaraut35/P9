package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.database.RealEStateDao
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.models.RealEstate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Singleton

//to expose data to another application


class DatabaseContentProvider   : ContentProvider() {
    //@Inject constructor(private val realEstateDatabase: RealEstateDatabase)


    //entry point of content provider to init variabeml
    override fun onCreate(): Boolean {
        return true
    }

    //take an uri as input and retireve the data (by a cursor) from room
    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {

        if (context != null) {
            val realEstateId = ContentUris.parseId(p0).toInt()
            //val cursor = realEstateDao.getRealEstateWithCursor(realEstateId)
            //return cursor
            return null
        }

        throw IllegalArgumentException("Failed to query row uri $p0")
return null
    }

    //return the type MIME associated with the URI to identify the type of data
    override fun getType(p0: Uri): String? {
        return null
   //     return "vnd.android.cursor.realEstate/com.openclassrooms.realestatemanager.provider." + RealEstate::class.java.simpleName
    }

    //take a URIO as input and insert data in ContentValues format into the destination room
    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
       return null
    }

    //take an uri as input adn delete in ContentValues format ffrom room
    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        return 0
    }

    //take a URI as input and update dat ainCOntent Values in the destination room
    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        return 0
    }
}