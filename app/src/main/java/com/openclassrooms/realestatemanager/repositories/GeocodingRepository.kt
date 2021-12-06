package com.openclassrooms.realestatemanager.repositories

import android.util.Log
import com.openclassrooms.realestatemanager.api.GoogleGeocoding
import com.openclassrooms.realestatemanager.api.ResponseGeocoding
import com.openclassrooms.realestatemanager.database.RealEStateDao
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class GeocodingRepository @Inject constructor(private val googleGeocoding : GoogleGeocoding) {

   private fun getResult()  = googleGeocoding.getLatLngByAddress().enqueue(object : Callback<ResponseGeocoding>{
      override fun onResponse(
         call: Call<ResponseGeocoding>,
         response: Response<ResponseGeocoding>
      ) {

         if (response.isSuccessful){
            Log.i("[API]","" + response.body().toString())
         }

      }

      override fun onFailure(call: Call<ResponseGeocoding>, t: Throwable) {

      }

   })

   init {
       getResult()
   }




   fun getLatLngByAddress() {
      getResult()
   }


}

