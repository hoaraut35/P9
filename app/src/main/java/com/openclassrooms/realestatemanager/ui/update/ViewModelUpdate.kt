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
        localDatabaseRepository.getFlowRealEstateFullById(sharedRepository.getPropertyId())
            .asLiveData()

    fun insertMedia(media: RealEstateMedia) =
        viewModelScope.launch { localDatabaseRepository.insertMedia(media) }

    var getLAstRowId = localDatabaseRepository.getLastRowId().asLiveData()
    var getLastRowIdForMedia = localDatabaseRepository.getLastRowIdForMedia().asLiveData()


    //the starting list...
    var initialListOfMedia: MutableList<RealEstateMedia> = mutableListOf()
    var newListOfMedia: MutableList<RealEstateMedia> = mutableListOf()



    //the new list...
    private var mutableListOfMedia = MutableLiveData<List<RealEstateMedia>>()

    //actual list of media...
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


    //to delete media from the list
    fun deleteMedia(media: RealEstateMedia) {
        listOfMedia.remove(media)
        listOfMediaToRemove.add(media)
        mutableListOfMedia.value = listOfMedia
        //delete it in database...
        viewModelScope.launch { localDatabaseRepository.deleteMedia(media) }

    }

    //function to publish UI to fragment
    fun getMediaListFromVM(): LiveData<List<RealEstateMedia>> {
        return mutableListOfMedia
    }

    fun updateRealEstate(realEstate: RealEstate) {
        viewModelScope.launch { localDatabaseRepository.updateRealEstate(realEstate) }
    }


    val mediaList: MutableList<RealEstateMedia> = mutableListOf()

    //update the title in the list  of media...
    fun updateMediaTitle(title: String, uri: String) {

        for (media in listOfMedia) {
            if (media.uri == uri) {
                media.name = title
            }
            if (!mediaList.contains(media)) {
                mediaList.add(media)
            }
        }

    }


}


