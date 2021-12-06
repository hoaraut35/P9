package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.BuildConfig
import retrofit2.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleGeocoding  {

    @GET("geocode/json?language=fr&key=${BuildConfig.GOOGLE_MAP_KEY}")
    fun getLatLngByAddress(@Query("address") address: String): Call<ResponseGeocoding>

}