package com.openclassrooms.realestatemanager.ui.updatenew

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import com.openclassrooms.realestatemanager.repositories.Shared
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelUpdate @Inject constructor(
    private val localDatabaseRepository: LocalDatabaseRepository,
    private val shared: Shared
) :
    ViewModel() {

    private var estate = localDatabaseRepository.getRealEstate(shared.getPropertyId())

    fun getEstate(): LiveData<RealEstate> {
        return estate
    }

}


