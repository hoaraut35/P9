package com.openclassrooms.realestatemanager.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.repositories.Shared
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelDetail @Inject constructor(private val shared: Shared) : ViewModel() {

   // var propertyId: Int = 0

    @JvmName("setPropertyId1")
    fun setPropertyId(id: Int) {
      //   id
      //  Log.i("[DETAIL]", "" + propertyId)
        shared.setPropertyID(id)
    }


}