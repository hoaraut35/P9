package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.BuildConfig
import retrofit2.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleGeocoding  {

    @GET("geocode/json?address=24%20Sussex%20Drive%20Ottawa%20ON&key=${BuildConfig.GOOGLE_MAP_KEY}&language=fr")
    fun getLatLngByAddress(): Call<ResponseGeocoding>

}