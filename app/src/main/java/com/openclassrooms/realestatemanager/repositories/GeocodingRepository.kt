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

   // private lateinit var geocodingResponse: ResponseGeocoding
    private var mutableLiveDataOfGeocoding = MutableLiveData<ResponseGeocoding>()

    fun getLatLngLiveData(): LiveData<ResponseGeocoding> {
        return mutableLiveDataOfGeocoding
    }

    fun getLatLngAddress(address: String, realEstateId: Int) {
        googleGeocoding.getLatLngByAddress(address).enqueue(object : Callback<ResponseGeocoding> {
            override fun onResponse(
                call: Call<ResponseGeocoding>,
                response: Response<ResponseGeocoding>
            ) {
                if (response.isSuccessful) {
                    response.body()!!.idRealEstate = realEstateId
                    //geocodingResponse = response.body()!!
                    //mutableLiveDataOfGeocoding.value = geocodingResponse
                    mutableLiveDataOfGeocoding.value = response.body()!!
                }
            }

            override fun onFailure(call: Call<ResponseGeocoding>, t: Throwable) {
            }
        })
    }
}