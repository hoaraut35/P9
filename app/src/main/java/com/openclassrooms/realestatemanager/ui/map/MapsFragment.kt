package com.openclassrooms.realestatemanager.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentMapsBinding
import com.openclassrooms.realestatemanager.ui.detail.RealEstateDetailFragment
import dagger.hilt.android.AndroidEntryPoint

private const val LOCATION_REQUEST_INTERVAL_MS = 10000
private const val SMALLEST_DISPLACEMENT_THRESHOLD_METER = 100f
const val REQUEST_CHECK_SETTINGS = 111

@AndroidEntryPoint
class MapsFragment : Fragment(), ActivityCompat.OnRequestPermissionsResultCallback {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private val viewModelMap by viewModels<ViewModelMap>()
    private var myGoogleMap: GoogleMap? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        myGoogleMap = googleMap
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        setupMarkerCLick()
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

        viewModelMap.getRealEstateFull().observe(viewLifecycleOwner) { listRealEstate ->

            for (realEstate in listRealEstate) {

                if (realEstate.realEstateFullData.address!!.lat != null && realEstate.realEstateFullData.address!!.lng != null) {
                    MapsUtils.addMarker(
                        requireContext(),
                        myGoogleMap,
                        realEstate.realEstateFullData.address!!.lat!!,
                        realEstate.realEstateFullData.address!!.lng!!,
                        realEstate.realEstateFullData.typeOfProduct.toString(),
                        realEstate.realEstateFullData.realEstateId
                    )
                } else {

                    if (realEstate.realEstateFullData.address!!.street_name != null &&
                        realEstate.realEstateFullData.address!!.city != null &&
                        realEstate.realEstateFullData.address!!.zip_code != null
                    ) {
                        //TODO: bug on address
                        //val address = "11 rue du vieux moulin 35220 Saint Didier"
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

        return rootView
    }

    //TODO: see to automatic update
    @SuppressLint("MissingPermission")
    private fun startGPS() {

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                viewModelMap.latLng = viewModelMap.setLocation(location)
                setupMyLocation(viewModelMap.latLng!!)
                setupZoom()
            }
        }

    }

    @SuppressLint("MissingPermission")
    private fun startGPS2() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setSmallestDisplacement(SMALLEST_DISPLACEMENT_THRESHOLD_METER)
                .setInterval(LOCATION_REQUEST_INTERVAL_MS.toLong()),
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            viewModelMap.latLng = viewModelMap.setLocation(locationResult.lastLocation)
            setupMyLocation(viewModelMap.latLng!!)
            setupZoom()

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == 0 && grantResults[0] != -1) {
            MapsUtils.checkGpsState(requireContext(),requireActivity())
            startGPS2()

        } else {
        //    startGPS()
        }
    }

    private fun setupMarkerCLick() {

        myGoogleMap!!.setOnMarkerClickListener {
            //we get the id by marker tag
            val realEstateId = it.tag
            //we set bundle
            val bundle = Bundle()
            bundle.putString(RealEstateDetailFragment.ARG_REAL_ESTATE_ID, realEstateId.toString())
            //we call fragment
            findNavController().navigate(
                R.id.action_mapsFragment_to_realEstateDetailFragment,
                bundle
            )
            false
        }
    }

    @SuppressLint("MissingPermission")
    private fun setupZoom() {
        myGoogleMap?.uiSettings?.isZoomControlsEnabled = true
        myGoogleMap?.isMyLocationEnabled = true
    }

    private fun setupMyLocation(latLng: LatLng) {

        val cameraPosition =
            CameraPosition.Builder().target(latLng)
                .zoom(8f).tilt(30f).bearing(0f).build()
        myGoogleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}