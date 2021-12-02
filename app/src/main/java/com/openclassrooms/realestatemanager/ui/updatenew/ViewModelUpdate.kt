package com.openclassrooms.realestatemanager.ui.updatenew

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import com.openclassrooms.realestatemanager.models.RealEstateWithMedia
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


    private var mutableListOfMedia = MutableLiveData<List<RealEstateMedia>>()

    private val listOfMedia: MutableList<RealEstateMedia> = mutableListOf()

    private var estate = localDatabaseRepository.getRealEstate(shared.getPropertyId())

   // private var estateWithMedia : Livedata<List<RealEstateWithMedia>> = localDatabaseRepository.getCurrentRealEstateWithMedia(shared.getPropertyId()).asLiveData()



    fun getEstate(): LiveData<RealEstate> {
        return estate
    }

//    fun getEstateWithMedia(): LiveData<List<RealEstateWithMedia>> {
//        return estateWithMedia
//    }




    fun addMediaToList(media: RealEstateMedia) {
        listOfMedia.add(media)
      //  sortMedia()
        mutableListOfMedia.value = listOfMedia
        Log.i("[MEDIA]", "Data from viewmodel list : $listOfMedia")
    }


    fun deleteMedia(media: RealEstateMedia) {
        listOfMedia.remove(media)
        mutableListOfMedia.value = listOfMedia
    }


    //function to publish UI to fragment
    fun getUIToShow(): LiveData<List<RealEstateMedia>> {
        return mutableListOfMedia
    }

}


