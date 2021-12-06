package com.openclassrooms.realestatemanager.models

import androidx.room.Embedded
import androidx.room.Relation

//do not use
class RealEstateWithPOIs(
    @Embedded val realEstate: RealEstate,
    @Relation(
        parentColumn = "realEstateId",
        entityColumn = "realEstateParentId"
    )
    val poi: RealEstatePOI
)