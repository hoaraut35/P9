package com.openclassrooms.realestatemanager.ui.create

import android.util.Log
import androidx.lifecycle.*
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

    private var mutableListOfMedia = MutableLiveData<List<RealEstateMedia>>()
    private val listOfMedia: MutableList<RealEstateMedia> = mutableListOf()

    val realEstateVM : RealEstate = RealEstate()
    val listOfChip : MutableList<String> = mutableListOf()
    val chip  : String? = null

    fun insertPOI(poi:RealEstatePOI) = viewModelScope.launch { localDatabaseRepository.insertPOI(poi) }

    //update photo or video to database
    fun addMediaToList(media: RealEstateMedia) {
        listOfMedia.add(media)
        sortMedia()
        mutableListOfMedia.value = listOfMedia
        Log.i("[MEDIA]", "Data from viewmodel list : $listOfMedia")
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

    fun propertyTypeChanged(type: String) {
        Log.i("[PROPERTY]", "Type of property : $type")
    }

    fun deleteMedia(media: RealEstateMedia) {
        listOfMedia.remove(media)
        mutableListOfMedia.value = listOfMedia
    }

}



