package com.openclassrooms.realestatemanager.models

data class RealEstateAddress(
    val street_name: String? = null,
    val street_number: String? = null,
    val city: String? = null,
    val zip_code: Int? = null
)