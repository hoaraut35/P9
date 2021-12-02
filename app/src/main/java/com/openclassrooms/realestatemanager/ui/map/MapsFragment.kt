package com.openclassrooms.realestatemanager.ui.map

import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentMapsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private val viewModelMap by viewModels<ViewModelMap>()

    lateinit var myGoogleMap : GoogleMap

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        myGoogleMap = googleMap

        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        setupZoom()




    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val rootView = binding.root

//        val locationPermissionRequest = registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { permissions ->
//            when {
//                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
//                    // Precise location access granted.
//                    Log.i("[POSITION]", "Position autorisée")
//                }
//                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
//                    // Only approximate location access granted.
//                    Log.i("[POSITION]", "Position approximative")
//                }
//                else -> {
//                    // No location access granted.
//                    Log.i("[POSITION]", "Position refusée ")
//                }
//            }
//        }
//
//        locationPermissionRequest.launch(arrayOf(
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION))

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            Log.i("[POSITION]", "Position " + location?.longitude + " " + location?.latitude)

            if (location != null){

                viewModelMap.latLng = viewModelMap.setLocation(location)

                setupMyLocation(  viewModelMap.latLng!!)

            }
        }

        return rootView
    }



    @SuppressLint("MissingPermission")
    private fun setupZoom(){
        myGoogleMap.uiSettings.isZoomControlsEnabled = true
        myGoogleMap.isMyLocationEnabled = true
    }

    private fun setupMyLocation(latLng: LatLng) {
        myGoogleMap.clear()
        myGoogleMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("My position")
        )
        val cameraPosition =
            CameraPosition.Builder().target(latLng)
                .zoom(14f).tilt(30f).bearing(0f).build()
        myGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))


        //myGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //myGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13f));
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}