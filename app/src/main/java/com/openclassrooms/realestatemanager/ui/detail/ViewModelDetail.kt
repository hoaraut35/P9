package com.openclassrooms.realestatemanager.ui.detail

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateFull
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import com.openclassrooms.realestatemanager.repositories.Shared
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelDetail @Inject constructor(
    private val shared: Shared,
    private val localDatabaseRepository: LocalDatabaseRepository
) : ViewModel() {

    var realEstate: RealEstate = RealEstate()

    //work
    fun getRealEstatesLiveData(): LiveData<List<RealEstate>> = localDatabaseRepository.getFlowRealEstates().asLiveData()
    //work
    fun getRealEstatesFullData(): LiveData<List<RealEstateFull>> = localDatabaseRepository.getFlowRealEstatesFull().asLiveData()
    //work
    fun getRealEstateFullById(id:Int) : LiveData<RealEstateFull> = localDatabaseRepository.getFlowRealEstateFullById(id).asLiveData()

    //work with one champ
    fun updateRealEstateTest(realEstate: RealEstate) = viewModelScope.launch { localDatabaseRepository.updateRealEstateTest(realEstate) }

    //


        //var allRealEstate = localDatabaseRepository.getRealEstateJustOne().asLiveData()


    //ex
   // private var realEstate = MutableLiveData<List<RealEstate?>>()

    //ex
//    fun getRealEstate(id : Int) : MutableLiveData<List<RealEstate?>> {
//        loadUser(id)
//        return realEstate
//    }

    //ex
//    private fun loadUser(id: Int) {
//        //realEstate.value = localDatabaseRepository.getRealEstate(1).value
//        realEstate.value = allRealEstate.value
//
//    }


    private var myRealEstateIdFromUI: MutableLiveData<Int>? = MutableLiveData()

    private var myRealEstateWithFullData : MutableLiveData<RealEstateFull>? = MutableLiveData()

    //set id from UI
    fun setMyRealEstateIdFromUI(id: Int) {
        myRealEstateIdFromUI!!.value = id
//        myRealEstateWithFullData?.value  = localDatabaseRepository.getRealEstateFull(myRealEstateIdFromUI.toString().toInt()).asLiveData().value
    }

    //get id for UI
    fun getMyRealEstateIdFromUi(): MutableLiveData<Int> {
        return myRealEstateIdFromUI!!
    }

    //publish myRealEstateFullData to UI
    fun getPropertyFull(): MutableLiveData<RealEstateFull>? {
        return myRealEstateWithFullData
    }

    //list
    var realEstatesWithAllData = localDatabaseRepository.getFlowRealEstatesFull().asLiveData()

    //unique
    var realEstateFullData =
        localDatabaseRepository.getFlowRealEstateFullById(shared.getPropertyId()).asLiveData()

    private var myRealEstateFull: MutableLiveData<RealEstate>? = MutableLiveData()


    lateinit var myRealEstate: RealEstateFull

    fun setPropertyId(id: Int) {
        shared.setPropertyID(id)
        //  viewModelScope.launch { localDatabaseRepository.getRealEstateFull(shared .getPropertyId()) }

    }




    fun updateRealEstate(realEstate: RealEstate) =
        viewModelScope.launch { localDatabaseRepository.insertRealEstateTest(realEstate) }

}