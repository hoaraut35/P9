package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.PrimaryKey

//one to one
@Entity
data class RealEstatePOI(

    @PrimaryKey(autoGenerate = true) val poiId: Int?,
    var realEstateParentId: Int? = null,
    var school: Boolean? = null,
    var station: Boolean? = null,
    var park: Boolean? = null

)


