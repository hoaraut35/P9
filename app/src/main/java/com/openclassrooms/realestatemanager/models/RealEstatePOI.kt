package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.PrimaryKey

//one to one
@Entity
data class RealEstatePOI(
    @PrimaryKey(autoGenerate = false) val realEstateParentId: Int?,
    val school: Boolean? = null,
    val station :Boolean? = null,
    val park: Boolean? = null
)


