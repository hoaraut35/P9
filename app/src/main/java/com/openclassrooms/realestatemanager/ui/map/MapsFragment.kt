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
import androidx.lifecycle.Observer
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

    lateinit var myGoogleMap: GoogleMap

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val callback = OnMapReadyCallback { googleMap ->

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


        viewModelMap.getRealEstateFull().observe(viewLifecycleOwner, Observer {listRealEstate ->

            for (realEstate in listRealEstate) {

                if (realEstate.realEstateFullData.address!!.street_name != null && realEstate.realEstateFullData.address!!.city != null && realEstate.realEstateFullData.address!!.zip_code != null ) {

                    val address = "${realEstate.realEstateFullData.address!!.zip_code}"

                    viewModelMap.getResult(address)

                    Log.i("[API]", "" +  viewModelMap.getResult(address))

                }
            }
        })

        viewModelMap.getLatLngFromRepo()
            .observe(viewLifecycleOwner, Observer { listMArkersToCreate ->

                listMArkersToCreate.forEach { item ->
                    myGoogleMap.addMarker(
                        MarkerOptions()
                            .position(
                                LatLng(
                                    item.results!![0]!!.geometry!!.location!!.lat!!,
                                    item.results!![0]!!.geometry!!.location!!.lng!!
                                )
                            )
                            .title("Ma position")
                    )
                }
            })


        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            Log.i("[POSITION]", "Position " + location?.longitude + " " + location?.latitude)

            if (location != null) {

                viewModelMap.latLng = viewModelMap.setLocation(location)


                setupMyLocation(viewModelMap.latLng!!)

            }
        }


        //Log.i("[API]","" + viewModelMap.getResult())

        return rootView
    }


    private fun addMArker(lat: Double?, lng: Double?) {


    }


    @SuppressLint("MissingPermission")
    private fun setupZoom() {
        myGoogleMap.uiSettings.isZoomControlsEnabled = true
        myGoogleMap.isMyLocationEnabled = true
    }

    private fun setupMyLocation(latLng: LatLng) {
        //  myGoogleMap.clear()
//        myGoogleMap.addMarker(
//            MarkerOptions()
//                .position(latLng)
//                .title("My position")
//        )
//        val cameraPosition =
        CameraPosition.Builder().target(latLng)
            .zoom(14f).tilt(30f).bearing(0f).build()
        // myGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))


        //myGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //myGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13f));
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}