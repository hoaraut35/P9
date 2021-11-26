package com.openclassrooms.realestatemanager.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "realEstate_table")
data class RealEstate(

    @PrimaryKey(autoGenerate = true) val realEstateId: Int = 0,
    val typeOfProduct: String? = null,
    val cityOfProduct: String? = null,
    val price: Int? = null,
    val surface: Int? = null, //max 32767m²
    val numberOfRoom: Int? = null,
    val numberOfBathRoom: Int? = null,
    val numberOfBedRoom: Int? = null,
    val descriptionOfProduct: String? = null,
    @Embedded
    val address: RealEstateAddress? = null,
    val status: Boolean? = null,
    val dateOfEntry: String? = null,
    val releaseDate: String? = null,
    val agent: Int? = null

)