package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//https://developers.google.com/maps/documentation/geocoding/overview
interface GoogleGeocoding  {
    @GET("geocode/json?language=fr&key=${BuildConfig.GOOGLE_MAP_KEY}")
    fun getLatLngByAddress(@Query("address") address: String): Call<ResponseGeocoding>
}