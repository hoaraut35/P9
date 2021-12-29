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

    fun getRealEstateFullById(id: Int): LiveData<RealEstateFull> =
        localDatabaseRepository.getRealEstateFullById(id).asLiveData()

    fun setMyRealEstateIdFromUI(id: Int) {
        myRealEstateIdFromUI!!.value = id
    }

    fun setPropertyId(id: Int) {
        sharedRepository.setPropertyID(id)
    }

    fun updateRealEstate(realEstate: RealEstate) {
        viewModelScope.launch {
            localDatabaseRepository.insertRealEstate(realEstate)
        }
    }
}