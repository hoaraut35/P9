package com.openclassrooms.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.models.RealEstate

@Database(entities = [RealEstate::class], version = 1, exportSchema = false)
abstract class RealEstateDatabase : RoomDatabase() {

    abstract fun realEstateDao(): RealEStateDao //

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: RealEstateDatabase? = null

        fun getDatabase(context: Context): RealEstateDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RealEstateDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }


   /* class Callback @Inject constructor(
        private val database: Provider<RealEstateDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {


        //load just once for init data
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().realEstateDao()

            applicationScope.launch {
                dao.insert(RealEstate( typeOfProduct = "toto"))
                dao.insert(RealEstate(typeOfProduct =  "toto"))
                dao.insert(RealEstate(typeOfProduct =  "toto"))
                dao.insert(RealEstate( typeOfProduct = "toto"))
            }

        }
    }

    */



}