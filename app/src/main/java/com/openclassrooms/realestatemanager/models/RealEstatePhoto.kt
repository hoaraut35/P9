package com.openclassrooms.realestatemanager.models

import androidx.room.*

@Entity
data class RealEstatePhoto(

    @PrimaryKey(autoGenerate = true) val photo_id: Int = 0,
    val realEstateParentId: Int?,
    var name: String?,
    var uri: String?

)