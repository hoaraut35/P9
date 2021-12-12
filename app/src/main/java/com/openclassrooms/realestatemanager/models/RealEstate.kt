package com.openclassrooms.realestatemanager.models

import android.content.ContentValues
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "realEstate_table")
data class RealEstate(

    @PrimaryKey(autoGenerate = true) var realEstateId: Int = 0,
    var typeOfProduct: String? = null,
    var price: Int? = null,
    var surface: Int? = null, //max 32767m²
    var numberOfRoom: Int? = null,
    var numberOfBathRoom: Int? = null,
    var numberOfBedRoom: Int? = null,
    var descriptionOfProduct: String? = null,
    @Embedded
    var address: RealEstateAddress? = null,
    var status: Boolean? = null,
    var dateOfEntry: String? = null,
    var releaseDate: String? = null,
    var staticMapUri: String? = null,
    @Embedded
    var agent: RealEstateAgent? = null

){

    //translation for coontent provider
    fun fromContentValues(values : ContentValues):RealEstate{
        val realEstate:RealEstate = RealEstate()

        if (values.containsKey("typeOfProduct")) {
            realEstate.typeOfProduct = values.getAsString("typeOfProduct")
        }
        //add other fields...

        return realEstate
    }

}