package com.openclassrooms.realestatemanager.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.models.RealEstateFull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Shared @Inject constructor() {

    private var propertyId: Int = 0
    private var myRealEstateSearchList : MutableLiveData<MutableList<RealEstateFull>> = MutableLiveData()

    fun setPropertyID(int: Int) {
        propertyId = int
        Log.i("[DETAIL]", "from repo : " + propertyId)
    }

    @JvmName("getPropertyId1")
    fun getPropertyId(): Int {
        return propertyId
    }

    fun setResultListFromSearch(list: MutableList<RealEstateFull>) {
        myRealEstateSearchList.value = list
        Log.i("[SQLSHARED]", "retour $myRealEstateSearchList")
    }

    fun getResultListFromSearch(): LiveData<MutableList<RealEstateFull>> {
        return myRealEstateSearchList
    }

    fun clearResult(){
        myRealEstateSearchList.value?.clear()
    }

}