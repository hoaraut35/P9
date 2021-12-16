package com.openclassrooms.realestatemanager.ui.detail

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateFull
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import com.openclassrooms.realestatemanager.repositories.SharedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val sharedRepository: SharedRepository,
    private val localDatabaseRepository: LocalDatabaseRepository
) : ViewModel() {

    var actualRealEstate: RealEstate = RealEstate()
    private var myRealEstateIdFromUI: MutableLiveData<Int>? = MutableLiveData()
  //  private var myRealEstateWithFullData: MutableLiveData<RealEstateFull>? = MutableLiveData()

    //work
//    fun getRealEstatesLiveData(): LiveData<List<RealEstate>> =
//        localDatabaseRepository.getFlowRealEstates().asLiveData()

//    //work
//    fun getRealEstatesFullData(): LiveData<List<RealEstateFull>> =
//        localDatabaseRepository.getFlowRealEstatesFull().asLiveData()

    //work
    fun getRealEstateFullById(id: Int): LiveData<RealEstateFull> =
        localDatabaseRepository.getFlowRealEstateFullById(id).asLiveData()

//    //work with one champ
//    fun updateRealEstateTest(realEstate: RealEstate) =
//        viewModelScope.launch { localDatabaseRepository.updateRealEstateTest(realEstate) }


    //set id from UI
    fun setMyRealEstateIdFromUI(id: Int) {
        myRealEstateIdFromUI!!.value = id
    }

//    //get id for UI
//    fun getMyRealEstateIdFromUi(): MutableLiveData<Int> {
//        return myRealEstateIdFromUI!!
//    }

//    //publish myRealEstateFullData to UI
//    fun getPropertyFull(): MutableLiveData<RealEstateFull>? {
//        return myRealEstateWithFullData
//    }


    fun setPropertyId(id: Int) {
        sharedRepository.setPropertyID(id)
        //  viewModelScope.launch { localDatabaseRepository.getRealEstateFull(shared .getPropertyId()) }
    }

    //call repository to update a RealEstate
    fun updateRealEstate(realEstate: RealEstate) {
        viewModelScope.launch {
            localDatabaseRepository.insertRealEstateTest(realEstate)
        }
    }
}