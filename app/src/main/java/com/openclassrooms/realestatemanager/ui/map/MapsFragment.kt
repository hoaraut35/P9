package com.openclassrooms.realestatemanager.ui.map

import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import android.os.Bundle
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
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentMapsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private val viewModelMap by viewModels<ViewModelMap>()
    private var myGoogleMap: GoogleMap? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        myGoogleMap = googleMap
        setupZoom()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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

        viewModelMap.getRealEstateFull().observe(viewLifecycleOwner) { listRealEstate ->

            for (realEstate in listRealEstate) {

                if (realEstate.realEstateFullData.address!!.lat != null && realEstate.realEstateFullData.address!!.lng != null) {
                    MapsUtils.addMarker(
                        requireContext(),
                        myGoogleMap,
                        realEstate.realEstateFullData.address!!.lat!!,
                        realEstate.realEstateFullData.address!!.lng!!,
                        realEstate.realEstateFullData.typeOfProduct.toString()
                    )
                } else {

                    if (realEstate.realEstateFullData.address!!.street_name != null &&
                        realEstate.realEstateFullData.address!!.city != null &&
                        realEstate.realEstateFullData.address!!.zip_code != null
                    ) {
                        //                    val address = "11 rue du vieux moulin 35220 Saint Didier"
                        val address =
                            "${realEstate.realEstateFullData.address!!.street_name}+${realEstate.realEstateFullData.address!!.zip_code.toString()}+${realEstate.realEstateFullData.address!!.city}"
                        //"${realEstate.realEstateFullData.address!!.street_number.toString()}+ "+" + ${realEstate.realEstateFullData.address!!.street_name}+ "+" + ${realEstate.realEstateFullData.address!!.zip_code.toString()} + "+" + ${realEstate.realEstateFullData.address!!.city}"
                        //"" + "%20" +
                        //""

                        viewModelMap.realEstate = realEstate.realEstateFullData

                        viewModelMap.getLatLngForUI(
                            address,
                            realEstate.realEstateFullData.realEstateId
                        )

                        break

                    }
                }
            }
        }

        viewModelMap.getLatLngForUI()
            .observe(viewLifecycleOwner) { responseGeocoding ->

                viewModelMap.realEstate.realEstateId = responseGeocoding.idRealEstate!!
                viewModelMap.realEstate.address?.lat =
                    responseGeocoding.results!![0]!!.geometry!!.location!!.lat!!
                viewModelMap.realEstate.address?.lng =
                    responseGeocoding.results[0]!!.geometry!!.location!!.lng!!

                if (viewModelMap.realEstate.address?.lat != null && viewModelMap.realEstate.address?.lng != null) {
                    viewModelMap.updateRealEstate(viewModelMap.realEstate)
                }

            }


        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                viewModelMap.latLng = viewModelMap.setLocation(location)
                setupMyLocation(viewModelMap.latLng!!)
            }
        }

        return rootView
    }


    @SuppressLint("MissingPermission")
    private fun setupZoom() {
        myGoogleMap?.uiSettings?.isZoomControlsEnabled = true
        myGoogleMap?.isMyLocationEnabled = true
    }

    private fun setupMyLocation(latLng: LatLng) {

        val cameraPosition =
            CameraPosition.Builder().target(latLng)
                .zoom(14f).tilt(30f).bearing(0f).build()
        myGoogleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))


        //myGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //myGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13f));
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}