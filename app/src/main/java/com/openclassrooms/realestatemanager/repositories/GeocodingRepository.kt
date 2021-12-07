package com.openclassrooms.realestatemanager.repositories

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

    //private var mutableListOfGPS = mutableListOf<ResponseGeocoding>()
    private lateinit var mutableListOfGPS: ResponseGeocoding

    //private var mutableListOfLocation = MutableLiveData<List<ResponseGeocoding>>()
    private var mutableListOfLocation = MutableLiveData<ResponseGeocoding>()


    //fun getLatLngList() : LiveData<List<ResponseGeocoding>>{
    fun getLatLngList(): LiveData<ResponseGeocoding> {
        return mutableListOfLocation
    }

    //get a LatLng for ViewModel
    fun getLatLngAddress(address: String, id: Int) {

        googleGeocoding.getLatLngByAddress(address).enqueue(object : Callback<ResponseGeocoding> {
            override fun onResponse(
                call: Call<ResponseGeocoding>,
                response: Response<ResponseGeocoding>
            ) {

                if (response.isSuccessful) {
                    response.body()!!.idRealEstate = id
                    mutableListOfGPS = response.body()!!
                    mutableListOfLocation.value = mutableListOfGPS
                }
            }

            override fun onFailure(call: Call<ResponseGeocoding>, t: Throwable) {
            }
        })
    }
}

