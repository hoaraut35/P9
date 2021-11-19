package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = RealEstate::class,
        parentColumns = ["id"],
        childColumns = ["realEstateId"]
    )]
)
data class RealEstatePhoto(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val realEstateId: Int,
    val name: String,
    val uri: String

)