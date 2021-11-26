package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RealEstatePOI(
    @PrimaryKey(autoGenerate = true) val poi_id: Int = 0,
    val realEstateParentId: Int?,
    val school: Boolean?,
    val shops :Boolean?
)


