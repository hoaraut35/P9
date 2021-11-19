package com.openclassrooms.realestatemanager.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.models.RealEstateAddress
import com.openclassrooms.realestatemanager.models.RealEstatePhoto


//used to convert data from and to
class Converters {

    //Convert list photo
    @TypeConverter
    fun fromRealEstatePhotosList(value: List<RealEstatePhoto>): String {
        val gson = Gson()
        val type = object : TypeToken<List<RealEstatePhoto>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toRealEstatePhotosList(value: String): List<RealEstatePhoto> {
        val gson = Gson()
        val type = object : TypeToken<List<RealEstatePhoto>>() {}.type
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