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

    //variable for mutable UI state
    private val myViewStateCreateUI = MutableLiveData<ViewStateCreate>()


    private val listOfMedia : MutableList<RealEstateMedia> = mutableListOf<RealEstateMedia>()


    fun getRealEstate() : LiveData<List<RealEstate>>{
        return localDatabaseRepository.getAllRealEstate().asLiveData()
    }

    var allRealEstate = localDatabaseRepository.getAllRealEstate().asLiveData()





    fun addMediaToList(media : RealEstateMedia){
        listOfMedia.add(media)
    }



    fun propertyTypeChanged(type: String){
        Log.i("[PROPERTY]","Type of property : $type")
    }

    fun removeMediaFromList(media: RealEstateMedia){

    }

    fun propertyPriceChanged(price : Int){

    }

    fun propertySurfaceChanged(surface : Int){

    }

    fun propertyRoomNumberChanged(roomNumber : Int){

    }

    fun propertyDescriptionChanged(description : String){

    }

    fun propertyStreetNumberChanged(streetNumber : Int){

    }

    fun propertyStreetName(streetName : String){

    }

    fun propertyZipCodeChanged(zipCode : Int){

    }

    fun propertyCityChanged(city : String){

    }

    fun saveProperty(){

    }






    //function to publish UI to fragment
    fun getUIToShow() : LiveData<ViewStateCreate> = myViewStateCreateUI

}



