package com.openclassrooms.realestatemanager.ui.map

import android.content.Context
import android.content.IntentSender
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.LocationManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.openclassrooms.realestatemanager.R

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





        fun isGpsEnabled(locationManager: LocationManager): Boolean {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }




        //
        fun checkGpsState(context: Context,contextActivity : FragmentActivity ) {

            val locationManager = ContextCompat.getSystemService(
                contextActivity,
                LocationManager::class.java
            )

            if (isGpsEnabled(locationManager!!)) {

                Toast.makeText(
                    context,
                    "getString(R.string.app_name)",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                val locationRequest = LocationRequest.create()
                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

                val settingsBuilder = LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest)
                settingsBuilder.setAlwaysShow(true) //this displays dialog box like Google Maps with two buttons - OK and NO,THANKS

                val task = LocationServices.getSettingsClient(contextActivity)
                    .checkLocationSettings(settingsBuilder.build())

                task.addOnCompleteListener { task1: Task<LocationSettingsResponse?> ->

                    try {
                        val response =
                            task1.getResult(ApiException::class.java)
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                    } catch (exception: ApiException) {
                        when (exception.statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                             // Location settings are not satisfied. But could be fixed by showing the
                                // user a dialog.
                                try {
                                    // Cast to a resolvable exception.
                                    val resolvable = exception as ResolvableApiException
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    resolvable.startResolutionForResult(
                                        contextActivity,
                                        REQUEST_CHECK_SETTINGS
                                    )
                                } catch (e: IntentSender.SendIntentException) {
                                    // Ignore the error.
                                } catch (e: ClassCastException) {
                                    // Ignore, should be an impossible error.
                                }
                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                        }
                    }
                }
            }
        }

    }
}