package com.openclassrooms.realestatemanager.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.models.RealEstateAddress
import com.openclassrooms.realestatemanager.models.RealEstatePOI
import com.openclassrooms.realestatemanager.models.RealEstateMedia


//used to convert data from and to
class Converters {

    //Convert poi list
    @TypeConverter
    fun fromRealEstatePOIList(value: List<RealEstatePOI>): String {
        val gson = Gson()
        val type = object : TypeToken<List<RealEstatePOI>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toRealEstatePOIList(value: String): List<RealEstatePOI> {
        val gson = Gson()
        val type = object : TypeToken<List<RealEstatePOI>>() {}.type
        return gson.fromJson(value, type)
    }



    //Convert list photo
    @TypeConverter
    fun fromRealEstatePhotosList(value: List<RealEstateMedia>): String {
        val gson = Gson()
        val type = object : TypeToken<List<RealEstateMedia>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toRealEstatePhotosList(value: String): List<RealEstateMedia> {
        val gson = Gson()
        val type = object : TypeToken<List<RealEstateMedia>>() {}.type
        return gson.fromJson(value, type)
    }

    //Convert list address
    @TypeConverter
    fun fromRealEstateAddressList(value: List<RealEstateAddress>): String {
        val gson = Gson()
        val type = object : TypeToken<List<RealEstateAddress>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toRealEstateAddressList(value: String): List<RealEstateAddress> {
        val gson = Gson()
        val type = object : TypeToken<List<RealEstateAddress>>() {}.type
        return gson.fromJson(value, type)
    }
}