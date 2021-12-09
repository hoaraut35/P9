package com.openclassrooms.realestatemanager.models

//embedded faster
data class RealEstateAddress(
    var street_name: String?,
    var street_number: Int?,
    var city: String?,
    var zip_code: Int?,
    val country: String?,
    var lat: Double? = null,
    var lng : Double? = null,
)