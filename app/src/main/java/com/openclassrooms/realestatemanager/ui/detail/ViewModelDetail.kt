package com.openclassrooms.realestatemanager.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateWithMedia
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


    var myRealEstate : RealEstate? = null

    @JvmName("setPropertyId1")
    fun setPropertyId(id: Int) {
        //   id
        //  Log.i("[DETAIL]", "" + propertyId)
        shared.setPropertyID(id)
    }

    fun updateRealEstate(realEstate: RealEstate) =
        viewModelScope.launch { localDatabaseRepository.insertRealEstate(realEstate) }


}