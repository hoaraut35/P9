package com.openclassrooms.realestatemanager.injection

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.database.RealEStateDao
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.models.RealEstate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import javax.inject.Qualifier
import javax.inject.Singleton

@Module //used for specific constructor
@InstallIn(SingletonComponent::class) //dependency for all application
object AppModule { //must be object

    //provide Dao
    @Provides
    fun providesDao(database: RealEstateDatabase) = database.realEstateDao()

    //Provide database
    @Provides
    @Singleton
    fun provideRealEstateDatabase(
        appContext : Application,
         callBack : RealEstateDatabase.Callback
    ) = Room.databaseBuilder(appContext, RealEstateDatabase::class.java, "realEstateDB")
            .fallbackToDestructiveMigration()
            .addCallback(callBack)
            .build()


    @Provides
    @ApplicationScope
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope