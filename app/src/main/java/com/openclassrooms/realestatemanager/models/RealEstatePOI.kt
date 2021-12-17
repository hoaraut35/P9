package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.PrimaryKey

//one to one
@Entity
data class RealEstatePOI(

    @PrimaryKey(autoGenerate = true) val poiId: Int =0,
    var realEstateParentId: Int?,
    var school: Boolean? ,
    var station: Boolean?,
    var park: Boolean?

)


