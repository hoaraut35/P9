package com.openclassrooms.realestatemanager.models

import androidx.room.Ignore

//embedded faster
data class RealEstateAddress(
    val street_name: String?,
    val street_number: Int?,
    val city: String?,
    val zip_code: Int?,
    val country: String?
)
