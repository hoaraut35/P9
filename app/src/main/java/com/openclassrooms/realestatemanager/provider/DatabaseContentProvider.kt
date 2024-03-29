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

//to expose data to another application...

class DatabaseContentProvider : ContentProvider() {

    companion object {
        private const val myAuthority: String = "com.openclassrooms.realestatemanager.provider"
        private val myTableName: String = RealEstate::class.java.simpleName
        val myUriItem: Uri = Uri.parse("content://$myAuthority/$myTableName")
    }

    //https://developer.android.com/training/dependency-injection/hilt-android
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface EntryPointForContentProvider {
        fun getRealEstateDao(): RealEStateDao
    }

    override fun onCreate(): Boolean {
        return true
    }


    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor {

        val app = context?.applicationContext ?: throw IllegalStateException()
        val hiltEntryPointForDao = EntryPointAccessors.fromApplication(app, EntryPointForContentProvider::class.java)
        val dao = hiltEntryPointForDao.getRealEstateDao().getRealEstateWithCursor()
        val cursor: Cursor = dao
        cursor.setNotificationUri(app.contentResolver, p0)
        return cursor
    }

    //return the type MIME associated with the URI to identify the type of data
    override fun getType(p0: Uri): String? {
        return null
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        return null
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        return 0
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        return 0
    }
}