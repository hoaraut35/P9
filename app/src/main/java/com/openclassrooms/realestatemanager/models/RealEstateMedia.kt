package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RealEstateMedia(
    @PrimaryKey(autoGenerate = true) val photo_id: Int = 0,
    val realEstateParentId: Int?,
    var name: String?,
    var uri: String?
)