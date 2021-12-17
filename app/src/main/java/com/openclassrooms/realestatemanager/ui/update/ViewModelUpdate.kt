package com.openclassrooms.realestatemanager.ui.update

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateFull
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import com.openclassrooms.realestatemanager.repositories.SharedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelUpdate @Inject constructor(
    private val localDatabaseRepository: LocalDatabaseRepository,
    private val sharedRepository: SharedRepository
) :
    ViewModel() {

    var realEstate: RealEstate = RealEstate()

    var listOfMediaToRemove: MutableList<RealEstateMedia> = mutableListOf()

    fun getRealEstateFullById(): LiveData<RealEstateFull> =
        localDatabaseRepository.getFlowRealEstateFullById(sharedRepository.getPropertyId()).asLiveData()

    fun insertMedia(media: RealEstateMedia) =
        viewModelScope.launch { localDatabaseRepository.insertRealEstateMedia(media) }

    var getLAstRowId = localDatabaseRepository.getLastRowId().asLiveData()

    var initialListOfMedia: MutableList<RealEstateMedia> = mutableListOf()
    private var mutableListOfMedia = MutableLiveData<List<RealEstateMedia>>()
    private val listOfMedia: MutableList<RealEstateMedia> = mutableListOf()

    fun getMutableListOfMedia(): MutableList<RealEstateMedia> {
        return listOfMedia
    }

    //1 we get the initial list from UI
    fun initList() {
        listOfMedia.addAll(initialListOfMedia)
        mutableListOfMedia.value = listOfMedia
    }

    //to add media
    fun addMediaToList(media: RealEstateMedia) {
        listOfMedia.add(media)
        listOfMedia.sortBy { it.position }
        mutableListOfMedia.value = listOfMedia
    }


    //to delete media
    fun deleteMedia(media: RealEstateMedia) {
        listOfMedia.remove(media)
        mutableListOfMedia.value = listOfMedia
        viewModelScope.launch { localDatabaseRepository.deleteMedia(media) }

    }

    //function to publish UI to fragment
    fun getMediaListFromVM(): LiveData<List<RealEstateMedia>> {
        return mutableListOfMedia
    }

    fun updateRealEstate(realEstate: RealEstate) {
        viewModelScope.launch { localDatabaseRepository.updateRealEstate(realEstate) }
    }


    //update title in list for photo or video
    fun updateMediaTitle(title: String, uri: String) {
        listOfMedia.find { it.uri == uri }?.name = title
    }

    val mediaList: MutableList<RealEstateMedia> = mutableListOf()

    fun setDescriptionTitle(description: String, uri: String) {
        for (media in listOfMedia) {
            if (media.uri == uri) {
                media.name = description
            }
            if (!mediaList.contains(media)) {
                mediaList.add(media)
            }
        }
    }

}


