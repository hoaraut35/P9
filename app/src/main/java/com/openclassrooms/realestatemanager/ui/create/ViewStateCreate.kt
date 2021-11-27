package com.openclassrooms.realestatemanager.ui.create

import com.openclassrooms.realestatemanager.models.RealEstateMedia

data class ViewStateCreate(


    val listOfMediaUI : MutableList<RealEstateMedia>,

    val price : Int,
    val surface: Int,
    val room_number: Int,
    val description: String,
    val street_number: String



)