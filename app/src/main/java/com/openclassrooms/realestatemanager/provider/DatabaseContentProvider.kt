package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.database.RealEStateDao
import com.openclassrooms.realestatemanager.models.RealEstate
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import java.lang.IllegalStateException

//to expose data to another application


class DatabaseContentProvider : ContentProvider() {

    companion object {
        //FOR DATA
        val AUTHORITY: String = "com.openclassrooms.realestatemanager.provider"
        val TABLE_NAME: String = RealEstate::class.java.simpleName
        val RI_ITEM: Uri = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
    }

    //https://developer.android.com/training/dependency-injection/hilt-android
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface EntryPointForContentProvider {
        fun getRealEstateDao(): RealEStateDao
    }


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

        val app = context?.applicationContext ?: throw IllegalStateException()
        val hiltEntryPointForDao = EntryPointAccessors.fromApplication(app, EntryPointForContentProvider::class.java)
        val dao = hiltEntryPointForDao.getRealEstateDao().getRealEstateWithCursor()

        val cursor : Cursor = dao
        cursor.setNotificationUri(app.contentResolver,p0)


        //throw IllegalArgumentException("Failed to query row uri $p0")
        return cursor
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