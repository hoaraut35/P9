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

    private var mutableLiveDataOfGeocoding = MutableLiveData<ResponseGeocoding>()

    //for observe...
    fun getLatLngLiveData(): LiveData<ResponseGeocoding> {
        return mutableLiveDataOfGeocoding
    }

    //for query API...
    fun getLatLngAddress(address: String, realEstateId: Int) {
        googleGeocoding.getLatLngByAddress(address).enqueue(object : Callback<ResponseGeocoding> {
            override fun onResponse(
                call: Call<ResponseGeocoding>,
                response: Response<ResponseGeocoding>
            ) {
                if (response.isSuccessful) {
                    response.body()!!.idRealEstate = realEstateId
                    mutableLiveDataOfGeocoding.value = response.body()!!
                }
            }

            override fun onFailure(call: Call<ResponseGeocoding>, t: Throwable) {
            }
        })
    }
}