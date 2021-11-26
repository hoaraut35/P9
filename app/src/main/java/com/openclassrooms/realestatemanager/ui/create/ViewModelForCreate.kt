package com.openclassrooms.realestatemanager.ui.create

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstatePhoto
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ViewModelForCreate @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository) :
    ViewModel() {

    //variable for mutable UI state
    private val myViewStateCreateUI = MutableLiveData<ViewStateCreate>()


    private val listOfMedia : MutableList<RealEstatePhoto> = mutableListOf<RealEstatePhoto>()


    fun getRealEstate() : LiveData<List<RealEstate>>{
        return localDatabaseRepository.getAllRealEstate().asLiveData()
    }

    var allRealEstate = localDatabaseRepository.getAllRealEstate().asLiveData()





    fun addMediaToList(media : RealEstatePhoto){
        listOfMedia.add(media)
    }



    fun propertyTypeChanged(type: String){
        Log.i("[PROPERTY]","Type of property : $type")
    }

    fun removeMediaFromList(media: RealEstatePhoto){

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



