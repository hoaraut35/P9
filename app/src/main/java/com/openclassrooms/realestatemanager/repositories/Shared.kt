package com.openclassrooms.realestatemanager.repositories

import android.util.Log
import com.openclassrooms.realestatemanager.database.RealEStateDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Shared  @Inject constructor(
    private val realEstateDao: RealEStateDao
    ) {

    private var propertyId : Int = 0

    fun setPropertyID(int : Int){
        propertyId = int
        Log.i("[DETAIL]", "from repo : " + propertyId)
    }

    @JvmName("getPropertyId1")
    fun getPropertyId() : Int{
        return propertyId
    }

}