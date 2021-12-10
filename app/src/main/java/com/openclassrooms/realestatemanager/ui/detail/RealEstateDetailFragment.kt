package com.openclassrooms.realestatemanager.ui.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstateDetailBinding
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class RealEstateDetailFragment : Fragment(), MyRequestImageListener.Callback {

    private var _binding: FragmentRealEstateDetailBinding? = null
    private val binding get() = _binding!!
    private val detailViewModel by viewModels<ViewModelDetail>()
    private var realEstateIdFromBundle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_REAL_ESTATE_ID)) {
                realEstateIdFromBundle = it.getString(ARG_REAL_ESTATE_ID)
                realEstateIdFromBundle?.let { it1 ->
                    detailViewModel.setPropertyId(it1.toInt())
                    detailViewModel.setMyRealEstateIdFromUI(it1?.toInt())
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRealEstateDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val recyclerViewMedias: RecyclerView = binding.recyclerviewMedias

        setHasOptionsMenu(true)

        detailViewModel.getRealEstateFullById(realEstateIdFromBundle!!.toInt())
            .observe(viewLifecycleOwner) { RealEstateObserved ->

                //*********************************************************************************

                detailViewModel.actualRealEstate = RealEstateObserved.realEstateFullData

                //*********************************************************************************

                binding.textType?.text = RealEstateObserved.realEstateFullData.typeOfProduct
                binding.textPrice?.text =
                    RealEstateObserved.realEstateFullData.price.toString() + "€"
                binding.textSurface?.text =
                    RealEstateObserved.realEstateFullData.surface.toString() + "m²"

                binding.textNumberRoom?.text =
                    RealEstateObserved.realEstateFullData.numberOfRoom.toString() + " Room"
                binding.textNumberBathroom?.text =
                    RealEstateObserved.realEstateFullData.numberOfBathRoom.toString() + " Bathroom"
                binding.textNumberBedroom?.text =
                    RealEstateObserved.realEstateFullData.numberOfBedRoom.toString() + " Bedroom"

                binding.textDescription?.text =
                    RealEstateObserved.realEstateFullData.descriptionOfProduct

                RealEstateObserved.mediaList?.let { myRealEstateMediaList ->
                    setupRecyclerView(recyclerViewMedias, myRealEstateMediaList)
                }

                binding.textStreetNumber?.text =
                    RealEstateObserved.realEstateFullData.address?.street_number.toString()
                binding.textStreetName?.text =
                    RealEstateObserved.realEstateFullData.address?.street_name
                binding.textZipCode?.text =
                    RealEstateObserved.realEstateFullData.address?.zip_code.toString()
                binding.textCityName?.text = RealEstateObserved.realEstateFullData.address?.city

                //TODO: add POI here...

                when (RealEstateObserved.realEstateFullData.status) {
                    true -> binding.textState?.text = "Vendu"
                    false -> binding.textState?.text = "A vendre"
                    else -> binding.textState?.text = "Etat Inconnu"
                }

                if (!RealEstateObserved.realEstateFullData.dateOfEntry.isNullOrEmpty()) {
                    binding.textSaleDate?.text = RealEstateObserved.realEstateFullData.dateOfEntry
                }

                if (!RealEstateObserved.realEstateFullData.releaseDate.isNullOrEmpty()) {
                    binding.textDateOfSale?.text = RealEstateObserved.realEstateFullData.releaseDate
                }

                binding.textAgent?.text =
                    "${RealEstateObserved.realEstateFullData.agent?.agent_firstName} ${RealEstateObserved.realEstateFullData.agent?.agent_lastName}"

                //**********************************************************************************

                val addressForApi =
                    RealEstateObserved.realEstateFullData.address?.city + "+" + RealEstateObserved.realEstateFullData.address?.zip_code + "+" + RealEstateObserved.realEstateFullData.address?.street_name + "+" + RealEstateObserved.realEstateFullData.address?.street_number.toString()

                val urlForApi =
                    "https://maps.googleapis.com/maps/api/staticmap?center=" + addressForApi + "&zoom=15&size=600x300&maptype=roadmap" +
                            "&key=" + BuildConfig.GOOGLE_MAP_KEY;

                val imageViewForMap = binding.imageMap

                //if we have already an uri in dataBase then we load the local image ...
                if (!RealEstateObserved.realEstateFullData.staticMapUri.isNullOrEmpty()) {
                    Glide.with(this)
                        .load(RealEstateObserved.realEstateFullData.staticMapUri)
                        .error(R.drawable.ic_baseline_error_24).into(binding.imageMap)
                } else {
                    Glide
                        .with(this)
                        .load(urlForApi)
                        .centerCrop()
                        .listener(MyRequestImageListener(this))//called to create a bitmap on internal storage
                        .into(imageViewForMap)
                }

                //*********************************************************************************
            }
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        when (realEstateIdFromBundle != "") {
            true -> menu.findItem(R.id.realEstateUpdateBtnNew).isVisible = true
            else -> menu.findItem(R.id.realEstateUpdateBtnNew).isVisible = false
        }
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        myRealEstateWithMediaList: List<RealEstateMedia>
    ) {
        val myLayoutManager = LinearLayoutManager(activity)
        myLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = myLayoutManager
        recyclerView.adapter = RealEstateDetailAdapter(myRealEstateWithMediaList)
    }

    companion object {

        const val ARG_REAL_ESTATE_ID = "item_id"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RealEstateDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_REAL_ESTATE_ID, param1)
                }
            }
    }

    //Glide callBack
    override fun onFailure(message: String?) {
        //Nothing to do
    }

    //GlideCallback
    override fun onSuccess(dataSource: Drawable?) {

        //if we have a bitmap from google staticmap then...
        if (dataSource != null) {
            val staticMapBitmap = dataSource.toBitmap()
            val dateFileName = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val fileName = "StaticMapPhoto"
            val fileNameUri: String =
                context?.filesDir.toString() + "/" + "$fileName$dateFileName.jpg"

            if (DetailUtils.savePhotoToInternalMemory(
                    dateFileName,
                    fileName,
                    staticMapBitmap,
                    requireContext()
                )
            ) {
                //we set the uri for staticmapBitmap
                detailViewModel.actualRealEstate.staticMapUri = fileNameUri
                //we update data into database
                detailViewModel.updateRealEstate(detailViewModel.actualRealEstate)
            }
        }
    }
}