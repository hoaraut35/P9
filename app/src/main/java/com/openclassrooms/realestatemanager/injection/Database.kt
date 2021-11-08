package com.openclassrooms.realestatemanager.injection

import android.content.Context
import androidx.room.Room
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class Database {

    @Provides
    fun providesDao(database: RealEstateDatabase) = database.realEstateDao()

    @Provides
    @Singleton
    fun provideRealEstateDatabase(@ApplicationContext appContext : Context): RealEstateDatabase {
        return Room.databaseBuilder(
            appContext,
            RealEstateDatabase::class.java,
            "realEstateDB"
        ).build()
    }

}






