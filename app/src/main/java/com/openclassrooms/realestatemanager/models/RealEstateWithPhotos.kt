package com.openclassrooms.realestatemanager.models

import androidx.room.Embedded
import androidx.room.Relation

data class RealEstateWithPhotos(
    @Embedded val realEstate: RealEstate,
    @Relation(
        parentColumn = "id",
        entityColumn = "realEstateId"
    )

    val photosList: List<RealEstatePhoto>

)