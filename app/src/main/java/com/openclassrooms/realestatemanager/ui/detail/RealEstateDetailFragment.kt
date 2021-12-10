package com.openclassrooms.realestatemanager.ui.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
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

    private var itemIdBundle: String? = null

    private lateinit var imageMap: ImageView
    private lateinit var imageUri2: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_REAL_ESTATE_ID)) {
                itemIdBundle = it.getString(ARG_REAL_ESTATE_ID)
                itemIdBundle?.let { it1 ->
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

        imageMap = binding.imageMap

        //realEstate with full data by id work
        detailViewModel.getRealEstateFullById(itemIdBundle!!.toInt()).observe(viewLifecycleOwner) {

            binding.textType?.text = it.realEstateFullData.typeOfProduct
            binding.textPrice?.text = it.realEstateFullData.price.toString() + "€"
            binding.textSurface?.text = it.realEstateFullData.surface.toString() + "m²"
            binding.textNumberRoom?.text = it.realEstateFullData.numberOfRoom.toString() + " Room"
            binding.textNumberBathroom?.text = it.realEstateFullData.numberOfBathRoom.toString() + " Bathroom"
            binding.textNumberBedroom?.text = it.realEstateFullData.numberOfBedRoom.toString() + " Bedroom"

            binding.textDescription?.text = it.realEstateFullData.descriptionOfProduct

            it.mediaList?.let { it1 ->
                if (recyclerViewMedias != null) {
                    setupRecyclerView(recyclerViewMedias, it1)
                }
            }

            binding.textStreetNumber?.text = it.realEstateFullData.address?.street_number.toString()
            binding.textStreetName?.text = it.realEstateFullData.address?.street_name
            binding.textZipCode?.text = it.realEstateFullData.address?.zip_code.toString()
            binding.textCityName?.text = it.realEstateFullData.address?.city

            //TODO: add POI here...

            binding.textState?.text = it.realEstateFullData.status?.toString() + " status"

            //TODO: add date of entry

            //TODO: add date of sold

            binding.textAgent?.text =  "${it.realEstateFullData.agent?.agent_firstName} ${it.realEstateFullData.agent?.agent_lastName}"


            val address =
                it.realEstateFullData.address?.city + "+" + it.realEstateFullData.address?.zip_code + "+" + it.realEstateFullData.address?.street_name

            imageUri2 =
                "https://maps.googleapis.com/maps/api/staticmap?center=" + address + "&zoom=15&size=600x300&maptype=roadmap" +
                        "&key=" + BuildConfig.GOOGLE_MAP_KEY;

            //we have an image, load from internal memory
            if (it.realEstateFullData.staticampuri != null) {
                binding.imageMap.let {
                    Glide.with(this).load(detailViewModel.realEstate.staticampuri)
                        .error(R.drawable.ic_baseline_error_24).into(it)
                }
            } else {
                //load map into first imageview
                Glide
                    .with(this)
                    .load(imageUri2)
                    .centerCrop()
                    .listener(MyRequestImageListener(this))
                    .into(imageMap)//end listener
            }

            detailViewModel.realEstate = it.realEstateFullData
        }
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        when (itemIdBundle != "") {
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

    override fun onFailure(message: String?) {
    }

    override fun onSuccess(dataSource: Drawable?) {

        if (dataSource != null) {
            val staticMapBitmap = dataSource.toBitmap()
            val dateFileName: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

            val fileName = "StaticMapPhoto"

            val fileNameUri: String =
                context?.filesDir.toString() + "/" + "$fileName$dateFileName.jpg"

            if (DetailUtils.savePhotoToInternalMemory(
                    dateFileName, "StaticMapPhoto", staticMapBitmap,
                    requireContext()
                )
            ) {
                detailViewModel.realEstate.staticampuri = fileNameUri
                detailViewModel.updateRealEstate(detailViewModel.realEstate)
            }
        }
    }
}