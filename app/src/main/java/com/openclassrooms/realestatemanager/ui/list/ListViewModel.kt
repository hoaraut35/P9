package com.openclassrooms.realestatemanager.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import com.openclassrooms.realestatemanager.repositories.SharedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository, private val sharedRepository: SharedRepository) :
    ViewModel() {

    fun getRealEstateFull() = localDatabaseRepository.getRealEstatesFullList().asLiveData()
    fun getResultListSearch() = sharedRepository.getResultListFromSearch()
    fun clearSearch() = sharedRepository.clearResult()

}