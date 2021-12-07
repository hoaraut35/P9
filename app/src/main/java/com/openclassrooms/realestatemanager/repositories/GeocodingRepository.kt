package com.openclassrooms.realestatemanager.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.api.GoogleGeocoding
import com.openclassrooms.realestatemanager.api.ResponseGeocoding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeocodingRepository @Inject constructor(private val googleGeocoding: GoogleGeocoding) {

    private var mutableListOfGPS = mutableListOf<ResponseGeocoding>()



    private var mutableListOfLocation = MutableLiveData<List<ResponseGeocoding>>()


    fun getLatLngList() : LiveData<List<ResponseGeocoding>>{
        return mutableListOfLocation
    }

    fun getLatLngAddress(address: String, id : Int) {

        googleGeocoding.getLatLngByAddress(address).enqueue(object : Callback<ResponseGeocoding> {
            override fun onResponse(
                call: Call<ResponseGeocoding>,
                response: Response<ResponseGeocoding>
            ) {

                if (response.isSuccessful) {
                    response.body()!!.idRealEstate = id
                    mutableListOfGPS.add(response.body()!!)
                    mutableListOfLocation.value = mutableListOfGPS
                    Log.i("jhkjkj","")
                }
            }

            override fun onFailure(call: Call<ResponseGeocoding>, t: Throwable) {
            }
        })
    }
}

