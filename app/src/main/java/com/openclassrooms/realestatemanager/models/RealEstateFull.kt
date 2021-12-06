package com.openclassrooms.realestatemanager.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation


data class RealEstateFull(

    @Embedded val realEstateFullData: RealEstate, //with address embedded + agent
    @Relation(
        parentColumn = "realEstateId",
        entityColumn = "realEstateParentId"
    )
    val mediaList: List<RealEstateMedia>? = null,

    @Relation(
        parentColumn = "realEstateId",
        entityColumn = "realEstateParentId"
    )
    val poi: RealEstatePOI? = null

)