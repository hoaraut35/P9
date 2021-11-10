package com.openclassrooms.realestatemanager.ui.master

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.database.RealEStateDao
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


//get data from repository for ui survive configuration change

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dao : RealEStateDao    ) : ViewModel() {



    fun getNumberOfRealEstate() : Int{
        return 10
    }

    fun getAllRealEstate(): LiveData<List<RealEstate>>{
        return dao.getAllRealEstate().asLiveData()
    }

    var realEstate = RealEstate(1,"test")

/*    fun addRealEstate(){
        localRepository.addRealEstate(realEstate)
    }

 */








}