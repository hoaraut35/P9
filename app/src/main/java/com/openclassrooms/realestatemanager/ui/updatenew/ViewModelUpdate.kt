package com.openclassrooms.realestatemanager.ui.updatenew

import android.util.Log
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateFull
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import com.openclassrooms.realestatemanager.models.RealEstatePOI
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import com.openclassrooms.realestatemanager.repositories.Shared
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelUpdate @Inject constructor(
    private val localDatabaseRepository: LocalDatabaseRepository,
    private val shared: Shared
) :
    ViewModel() {

    var realEstate: RealEstate = RealEstate()

    fun getRealEstateFullById() : LiveData<RealEstateFull> = localDatabaseRepository.getFlowRealEstateFullById(shared.getPropertyId()).asLiveData()



    var initialListOfMedia : MutableList<RealEstateMedia> = mutableListOf()
    private var mutableListOfMedia = MutableLiveData<List<RealEstateMedia>>()
    private val listOfMedia: MutableList<RealEstateMedia> = mutableListOf()

    fun getMutableListOfMedia(): MutableList<RealEstateMedia> {
        return listOfMedia
    }

    //1 we get the initial list from UI
    fun initList(){
        listOfMedia.addAll(initialListOfMedia)
        mutableListOfMedia.value = listOfMedia
    }

    //to add media
    fun addMediaToList(media: RealEstateMedia) {
        listOfMedia.add(media)
        listOfMedia.sortBy { it.uri }
        mutableListOfMedia.value = listOfMedia
    }


    //to delete media
    fun deleteMedia(media: RealEstateMedia) {
        listOfMedia.remove(media)
        mutableListOfMedia.value = listOfMedia
    }

    //function to publish UI to fragment
    fun getUIToShow(): LiveData<List<RealEstateMedia>> {
        return mutableListOfMedia
    }

    fun updateRealEstate(realEstate: RealEstate){
        viewModelScope.launch { localDatabaseRepository.updateRealEstate(realEstate)}
    }

}


