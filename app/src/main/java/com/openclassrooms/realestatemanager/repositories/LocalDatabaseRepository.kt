package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.database.RealEStateDao
import com.openclassrooms.realestatemanager.models.RealEstate


import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDatabaseRepository (private val realEstateDao: RealEStateDao
    ) {

 //   val allRealEstate : LiveData<List<RealEstate>> = realEstateDao.getAllRealEstate().asLiveData()

    //fun addRealEstate(realEstate: RealEstate){realEstateDao.add(realEstate)}

  //  fun getNumberOfRealEstate() = realEstateDao.getNumberOfRealEstate()

}