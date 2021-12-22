package com.openclassrooms.realestatemanager.models

import androidx.room.Embedded
import androidx.room.Relation

data class RealEstateFull(

    @Embedded val realEstateFullData: RealEstate, //with address embedded + agent
    @Relation(
        parentColumn = "realEstateId",
        entityColumn = "realEstateParentId"
    )
    //one to many
    val mediaList: List<RealEstateMedia>? = null,

    @Relation(
        parentColumn = "realEstateId",
        entityColumn = "realEstateParentId"
    )
    //one to one
    val poi: RealEstatePOI? = null

)