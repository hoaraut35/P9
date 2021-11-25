package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RealEstateVideo(

    @PrimaryKey(autoGenerate = true) val video_id: Int = 0,
    val realEstateParentId: Int?=null,
    var name: String? =null,
    var uri: String?
)