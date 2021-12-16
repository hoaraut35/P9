package com.openclassrooms.realestatemanager.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.models.RealEstateFull
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import com.openclassrooms.realestatemanager.repositories.Shared
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository,private val shared: Shared) :
    ViewModel() {

    var selectedEntryDate: Long? = null
    var selectedSoldDate: Long? = null

    var minPrice: Int? = null
    var maxPrice: Int? = null

    var minSurface: Int? = null
    var maxSurface: Int? = null

    var numberOfPhoto: Int? = null


    var mDate: LocalDate? = null

    fun getRealEstateFiltered(query: SupportSQLiteQuery) =
        localDatabaseRepository.getRealEstateFiltered(query).asLiveData()


    fun setResultListFromSearch(list : MutableList<RealEstateFull>){
        shared.setResultListFromSearch(list)
    }




}