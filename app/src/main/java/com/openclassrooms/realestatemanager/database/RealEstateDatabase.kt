package com.openclassrooms.realestatemanager.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class RealEstateDatabase : RoomDatabase() {
    abstract fun realEstateDao(): RealEStateDao

    companion object{

        @Volatile
        private var INSTANCE: RealEstateDatabase? = null

        fun getDabatase(context: Context): RealEstateDatabase{

            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RealEstateDatabase::class.java,
                    "mybase"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}