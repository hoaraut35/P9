package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RealEstate (

    @PrimaryKey(autoGenerate = true)
    val id : Int= 0,
    val typeOfProduct : String? = null
   // val price : Float? = null,
   // val surface : Short? = null, //max 32767m²
   // val numberOfRoom : Int? = null,
   // val descriptionOfProduct : String? = null,

    //tableau de photo
//
  //  val address : String? = null,

    //tableau de poi

  //  val statut : Boolean? = null,
  //  val dateCreateDocument : String? = null,
  //  val dateFinished : String? = null,
  //  val agent : Int?=null //FK from another table

)