package com.openclassrooms.realestatemanager.injection

import android.content.Context
import androidx.room.Room
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton


@Module //this container for injection
@InstallIn(SingletonComponent::class) //dependency for all application
object AppModule { //must be object

    //provide an injection of Dao
    @Provides
    @Singleton
    fun provideDao(database: RealEstateDatabase) = database.realEstateDao()

    //Provide database
    @Provides
    @Singleton  //to get just an instance
    fun provideDatabase(
        @ApplicationContext appContext: Context, //to get application context
       //callBack: RealEstateDatabase.Callback   //to add callback
    ) = Room.databaseBuilder(appContext, RealEstateDatabase::class.java, "realEstateDB")
        .fallbackToDestructiveMigration()
        //.addCallback(callBack)
        .build()

    //provide scope

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope