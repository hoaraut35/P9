package com.openclassrooms.realestatemanager.models

import androidx.room.*

@Entity
data class RealEstatePhoto(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val realEstateId: Int,
    val name: String,
    val uri: String

)