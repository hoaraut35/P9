package com.openclassrooms.realestatemanager.ui.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelForCreate @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository) :
    ViewModel() {

    //for media
    private var mutableListOfMedia = MutableLiveData<List<RealEstateMedia>>()
    private val listOfMedia: MutableList<RealEstateMedia> = mutableListOf()

    //update photo or video to database
    fun addMediaToList(media: RealEstateMedia) {
        listOfMedia.add(media)
        sortMedia()
        mutableListOfMedia.value = listOfMedia
        Log.i("[MEDIA]", "Data from viewmodel list : $listOfMedia")
    }

    private fun sortMedia(){
        listOfMedia.sortBy { it.uri}
    }

    //update title for photo or video
    fun updateMediaTitle(title: String, uri: String) {
        listOfMedia.find { it.uri == uri }?.name = title
    }

    //function to publish UI to fragment
    fun getUIToShow(): LiveData<List<RealEstateMedia>> {
        return mutableListOfMedia
    }

    //get all RealEstate from repository
    var allRealEstate = localDatabaseRepository.getAllRealEstate().asLiveData()

    //
    fun getRealEstate(): LiveData<List<RealEstate>> {
        return localDatabaseRepository.getAllRealEstate().asLiveData()
    }


    //remove photo or video from database
    fun removeMediaFromList(media: RealEstateMedia) {
        //listOfMedia.re
    }


    fun propertyTypeChanged(type: String) {
        Log.i("[PROPERTY]", "Type of property : $type")
    }


    fun propertyPriceChanged(price: Int) {

    }

    fun propertySurfaceChanged(surface: Int) {

    }

    fun propertyRoomNumberChanged(roomNumber: Int) {

    }

    fun propertyDescriptionChanged(description: String) {

    }

    fun propertyStreetNumberChanged(streetNumber: Int) {

    }

    fun propertyStreetName(streetName: String) {

    }

    fun propertyZipCodeChanged(zipCode: Int) {

    }

    fun propertyCityChanged(city: String) {

    }

    fun saveProperty() {

    }


}



