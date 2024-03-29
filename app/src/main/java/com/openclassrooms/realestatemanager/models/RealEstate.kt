package com.openclassrooms.realestatemanager.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "realEstate_table")
data class RealEstate(

    @PrimaryKey(autoGenerate = true) var realEstateId: Int = 0,
    var typeOfProduct: String? = null,
    var price: Int? = null,
    var surface: Int? = null,
    var numberOfRoom: Int? = null,
    var numberOfBathRoom: Int? = null,
    var numberOfBedRoom: Int? = null,
    var descriptionOfProduct: String? = null,
    @Embedded
    var address: RealEstateAddress? = null,
    var status: Boolean? = null,
    var dateOfEntry: Long? = null,
    var releaseDate: Long? = null,
    var staticMapUri: String? = null,
    var agent: String?=null

)