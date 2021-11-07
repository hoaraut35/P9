package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RealEstateAgent (

    @PrimaryKey
    val id : Int,
    val name: String? = null,
    val firstName : String? = null

)