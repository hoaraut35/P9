package com.openclassrooms.realestatemanager.ui.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

class MapsUtils {

    companion object {

        fun addMarker(
            context: Context,
            myGoogleMap: GoogleMap?,
            myLat: Double?,
            myLng: Double?,
            myTitle: String,
            realEstateId: Int
        ) {

            //option marker
            val myOptions = MarkerOptions()
            myOptions.position(LatLng(myLat!!, myLng!!))
            myOptions.title(myTitle)

            //marker
            val myMarker = myGoogleMap?.addMarker(myOptions)
            myMarker!!.tag = realEstateId
            // .icon(bitmapDescriptorFromVector(context, R.drawable.ic_baseline_house_24))

        }

        private fun bitmapDescriptorFromVector(
            context: Context,
            vectorResId: Int
        ): BitmapDescriptor {
            // below line is use to generate a drawable.
            val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

            // below line is use to set bounds to our vector drawable.
            vectorDrawable!!.setBounds(
                0,
                0,
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight
            )

            // below line is use to create a bitmap for our
            // drawable which we have added.
            val bitmap = Bitmap.createBitmap(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )

            // below line is use to add bitmap in our canvas.
            val canvas = Canvas(bitmap)

            // below line is use to draw our
            // vector drawable in canvas.
            vectorDrawable.draw(canvas)

            // after generating our bitmap we are returning our bitmap.
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }

    }
}