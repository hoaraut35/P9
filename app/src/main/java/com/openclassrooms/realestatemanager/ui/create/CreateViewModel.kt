package com.openclassrooms.realestatemanager.ui.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import com.openclassrooms.realestatemanager.models.RealEstatePOI
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository) :
    ViewModel() {

    //for insertion estate
    private var mutableLiveDataRowId = MutableLiveData<Long>()

    private var mutableListOfMedia = MutableLiveData<List<RealEstateMedia>>()
    private val listOfMedia: MutableList<RealEstateMedia> = mutableListOf()

    var realEstateVM  : RealEstate = RealEstate()

    //val listOfChip : MutableList<String> = mutableListOf()
    //val chip  : String? = null

    //insert POI in database
    fun insertPOI(poi:RealEstatePOI) = viewModelScope.launch { localDatabaseRepository.insertPOI(poi) }

    //update photo or video to database
    fun addMediaToList(media: RealEstateMedia) {
        listOfMedia.add(media)
        sortMedia()
        mutableListOfMedia.value = listOfMedia
    }

    //sort data for UI
    private fun sortMedia(){
        listOfMedia.sortBy { it.uri}
    }

    //update title in list for photo or video
    fun updateMediaTitle(title: String, uri: String) {
        listOfMedia.find { it.uri == uri }?.name = title
    }

    //function to publish UI to fragment
    fun getMediasListForUI(): LiveData<List<RealEstateMedia>> {
        //Transformations.map(mutableListOfMedia,  )
        return mutableListOfMedia
    }

    fun deleteMedia(media: RealEstateMedia) {
        listOfMedia.remove(media)
        mutableListOfMedia.value = listOfMedia
    }


    fun insertRealEstate(realEstate: RealEstate) = viewModelScope.launch {
        val myResult = localDatabaseRepository.insertRealEstate(realEstate)
        mutableLiveDataRowId.value = myResult
    }

    fun observeRowId() : LiveData<Long>{
        return mutableLiveDataRowId
    }
}



