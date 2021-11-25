package com.openclassrooms.realestatemanager.models

import androidx.room.Embedded
import androidx.room.Relation

data class RealEstateWithVideos(
    @Embedded
    val realEstate: RealEstate,
    @Relation(
        parentColumn = "realEstateId",
        entityColumn = "realEstateParentId"
    )
    val videosList: List<RealEstateVideo>
)
