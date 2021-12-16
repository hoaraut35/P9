package com.openclassrooms.realestatemanager.ui.search

import android.database.DatabaseUtils
import android.os.Build

class SearchUtils {

    companion object {



        public fun checkSQLiteVersion() : String{
            val version = android.database.sqlite.SQLiteDatabase.create(null).use {
                DatabaseUtils.stringForQuery(it, "SELECT sqlite_version()", null)
            }
            return "Android build :${Build.VERSION.SDK_INT} , SQLite version :$version"
        }
    }

}