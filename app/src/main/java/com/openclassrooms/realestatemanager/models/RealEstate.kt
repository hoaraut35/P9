package com.openclassrooms.realestatemanager.models

import android.content.ContentValues
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "realEstate_table")
data class RealEstate(

    @PrimaryKey(autoGenerate = true) var realEstateId: Int = 0,
    var typeOfProduct: String? = null,
    var cityOfProduct: String? = null,
    var price: Int? = null,
    var surface: Int? = null, //max 32767m²
    var numberOfRoom: Int? = null,
    var numberOfBathRoom: Int? = null,
    var numberOfBedRoom: Int? = null,
    var descriptionOfProduct: String? = null,
    @Embedded
    var address: RealEstateAddress? = null,
    var status: Boolean? = null,
    var dateOfEntry: String? = null,
    var releaseDate: String? = null,
    var agent: Int? = null

) {

//    @Ignore
//    constructor() : this(0) { }
//
//    constructor(typeOfProduct :String, cityOfProduct : String, price : Int,surface:Int,numberOfRoom: Int?,numberOfBathRoom: Int?,numberOfBedRoom: Int?,descriptionOfProduct: String?,address: RealEstateAddress?,status: Boolean?,dateOfEntry: String?,releaseDate: String?,agent: Int?): this(0){
//        this.typeOfProduct = typeOfProduct
//        this.cityOfProduct = cityOfProduct
//        this.price = price
//        this.surface = surface
//        this.numberOfBathRoom = numberOfBathRoom
//        this.numberOfBedRoom = numberOfBedRoom
//        this.numberOfRoom = numberOfRoom
//        this.descriptionOfProduct = descriptionOfProduct
//        this.address = address
//        this.status =status
//        this.dateOfEntry = dateOfEntry
//        this.releaseDate = releaseDate
//        this.agent = agent
//
//    }
//
//    companion object {
//
//        fun fromContentValues(values: ContentValues): RealEstate {
//            val realEstate = RealEstate()
//
//            //add other data here...
//            if (values.containsKey("realEstateID")) realEstate.realEstateId = values.getAsInteger("id")
//            if (values.containsKey("typeOfProduct")) realEstate.typeOfProduct = values.getAsString("typeOfProduct")
//            if (values.containsKey("cityOfProduct")) realEstate.cityOfProduct = values.getAsString("cityOfProduct")
//            if (values.containsKey("price")) realEstate.price = values.getAsInteger("price")
//            if (values.containsKey("surface")) realEstate.surface = values.getAsInteger("surface")
//            if (values.containsKey("numberOfRoom")) realEstate.numberOfRoom =
//                values.getAsInteger("numberOfRoom")
//            if (values.containsKey("numberOfBathRoom")) realEstate.numberOfBathRoom =
//                values.getAsInteger("numberOfBathRoom")
//            if (values.containsKey("numberOfBedRoom")) realEstate.numberOfBedRoom =
//                values.getAsInteger("numberOfBedRoom")
//            if (values.containsKey("descriptionOfProduct")) realEstate.descriptionOfProduct =
//                values.getAsString("descriptionOfProduct")
//            //   if (values.containsKey("address")) realEstate.address = values.getAsString("address")
//            if (values.containsKey("status")) realEstate.status = values.getAsBoolean("status")
//            if (values.containsKey("dateOfEntry")) realEstate.dateOfEntry =
//                values.getAsString("dateOfEntry")
//            if (values.containsKey("releaseDate")) realEstate.releaseDate =
//                values.getAsString("releaseDate")
//            if (values.containsKey("agent")) realEstate.agent = values.getAsInteger("agent")
//
//
//            return realEstate
//        }
//
//    }
//    //used by content provider to transform datza from database, change price to var !!!!!!!!!!!!
}