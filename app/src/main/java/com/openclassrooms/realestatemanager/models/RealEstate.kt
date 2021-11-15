package com.openclassrooms.realestatemanager.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "realEstate_table")
data class RealEstate(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val typeOfProduct: String? = null,
    val cityOfProduct: String? = null,
    val price: Int? = null,
    val surface: Int? = null, //max 32767m²
    val numberOfRoom: Int? = null,
    val numberOfBathRoom: Int? = null,
    val numberOfBedRoom: Int? = null,
    val descriptionOfProduct: String? = null,
    //see embedded room enttypeOfProductitiu

    //tableau de photo

    val photo : String?=null
    //  val address : String? = null,

    //tableau de poi

    //  val statut : Boolean? = null,
    //  val dateCreateDocument : String? = null,
    //  val dateFinished : String? = null,
    //  val agent : Int?=null //FK from another table

)