package com.openclassrooms.realestatemanager.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository) :
    ViewModel() {

    var selectedEntryDate: Long? = null
    var selectedSoldDate: Long? = null

    fun getRealEstateFiltered(query: SupportSQLiteQuery) =
        localDatabaseRepository.getRealEstateFiltered(query).asLiveData()


}