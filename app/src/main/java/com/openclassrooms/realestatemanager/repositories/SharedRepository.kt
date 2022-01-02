package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.models.RealEstateFull
import javax.inject.Inject
import javax.inject.Singleton

//used to share data between viewModels...

@Singleton
class SharedRepository @Inject constructor() {

    private var propertyId: Int = 0
    private var myRealEstateSearchList: MutableLiveData<MutableList<RealEstateFull>> =
        MutableLiveData()

    fun setPropertyID(int: Int) {
        propertyId = int
    }

    @JvmName("getPropertyId1")
    fun getPropertyId(): Int {
        return propertyId
    }

    //update the search list...
    fun setResultListFromSearch(list: MutableList<RealEstateFull>) {
        myRealEstateSearchList.value = list
    }

    //get the search list ...
    fun getResultListFromSearch(): LiveData<MutableList<RealEstateFull>> {
        return myRealEstateSearchList
    }

    //clear the search list...
    fun clearResult() = myRealEstateSearchList.value?.clear()

}