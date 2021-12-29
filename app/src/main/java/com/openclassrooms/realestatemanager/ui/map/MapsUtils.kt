package com.openclassrooms.realestatemanager.ui.map

import android.content.IntentSender
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task

class MapsUtils {

    companion object {

        fun addMarker(
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

        }

        private fun isGpsEnabled(locationManager: LocationManager): Boolean {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }

        fun checkGpsState(contextActivity : FragmentActivity ) {

            val locationManager = ContextCompat.getSystemService(
                contextActivity,
                LocationManager::class.java
            )

            if (!isGpsEnabled(locationManager!!)) {

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