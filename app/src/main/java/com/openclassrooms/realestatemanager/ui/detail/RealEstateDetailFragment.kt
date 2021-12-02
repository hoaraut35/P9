package com.openclassrooms.realestatemanager.ui.detail

import android.app.Activity.MODE_PRIVATE
import android.graphics.Bitmap
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
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import com.openclassrooms.realestatemanager.models.RealEstateWithMedia
import com.openclassrooms.realestatemanager.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class RealEstateDetailFragment : Fragment(), MyRequestImageListener.Callback {

    private val mainViewModel by viewModels<MainViewModel>()
    private val detailViewModel by viewModels<ViewModelDetail>()

    private var _binding: FragmentRealEstateDetailBinding? = null
    private val binding get() = _binding!!

    private var itemIdBundle: String? = null
    lateinit var imageMap: ImageView
    lateinit var imageUri2: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_REAL_ESTATE_ID)) {
                itemIdBundle = it.getString(ARG_REAL_ESTATE_ID)
                itemIdBundle?.let { it1 -> detailViewModel.setPropertyId(it1?.toInt()) }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRealEstateDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val recyclerViewMedias: RecyclerView? = binding.recyclerviewPhotos

        setHasOptionsMenu(true);

        mainViewModel.allRealEstateWithPhotos.observe(viewLifecycleOwner) { it ->

            val realEstate: RealEstateWithMedia? =
                it.find {
                    it.realEstate.realEstateId.toString() == itemIdBundle
                }

            if (realEstate != null) {

                detailViewModel.myRealEstate = realEstate.realEstate
                binding.textDescriptionDetail.setText(realEstate.realEstate.descriptionOfProduct)
                binding.textNumberStreet?.setText(realEstate.realEstate.address?.street_number.toString())
                binding.textStreetName?.setText(realEstate.realEstate.address?.street_name.toString())
                binding.textCityName?.setText(realEstate.realEstate.address?.city.toString())
                binding.textZipcode?.setText(realEstate.realEstate.address?.zip_code.toString())
                binding.textCountry?.setText(realEstate.realEstate.address?.country.toString())
                binding.textPrice?.setText(realEstate.realEstate.price.toString())
                binding.qtySurface?.setText(realEstate.realEstate.surface.toString())
                binding.qtyNumberBedroom.setText("Bedroom : " + realEstate.realEstate.numberOfBedRoom.toString())
                binding.qtyNumberBathroom.setText("Bathroom : " + realEstate.realEstate.numberOfBathRoom.toString())
                binding.qtyNumberRoom.setText("Room : " + realEstate.realEstate.numberOfRoom.toString())

                var address: String = realEstate.realEstate.address?.city.toString()
                recyclerViewMedias?.let { setupRecyclerView(it, realEstate.mediaList) }

                address =
                    realEstate.realEstate.address?.city + "+" + realEstate.realEstate.address?.zip_code + "+" + realEstate.realEstate.address?.street_name

                //https://guides.codepath.com/android/Displaying-Images-with-the-Picasso-Library

                imageUri2 =
                    "https://maps.googleapis.com/maps/api/staticmap?center=" + address + "&zoom=15&size=600x300&maptype=roadmap" +
                            "&key=" + BuildConfig.STATICMAP_KEY;

                //we have an image, load from internal memory
                if (realEstate.realEstate.staticampuri != null) {
                    binding.imageMap?.let {
                        Glide.with(this).load(realEstate.realEstate.staticampuri)
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
            }
        }

        return rootView
    }

    private fun savePhotoToInternalMemory(dateOfFile: String, bitmap: Bitmap): Boolean {

        return try {

            val fileName: String = "StaticMapPhoto"

            context?.openFileOutput("$fileName$dateOfFile.jpg", MODE_PRIVATE).use { stream ->

                //compress photo
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                    throw IOException("Compression fail")
                }

                val fileNameUri: String =
                    context?.filesDir.toString() + "/" + "$fileName$dateOfFile.jpg"

                //TODO: update just one column
                detailViewModel.updateRealEstate(
                    RealEstate(
                        realEstateId = itemIdBundle!!.toInt(),
                        staticampuri = fileNameUri,
                        typeOfProduct = detailViewModel.myRealEstate?.typeOfProduct,
                        cityOfProduct = detailViewModel.myRealEstate?.cityOfProduct,
                        price = detailViewModel.myRealEstate?.price,
                        surface = detailViewModel.myRealEstate?.surface,
                        numberOfRoom = detailViewModel.myRealEstate?.numberOfRoom,
                        descriptionOfProduct = detailViewModel.myRealEstate?.descriptionOfProduct
                    )
                )
            }
            true

        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        //if (itemIdBundle != null || itemIdBundle == "") {

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
        recyclerView.adapter = RealEstatePhotosAdapter(myRealEstateWithMediaList)
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
            savePhotoToInternalMemory("$dateFileName", staticMapBitmap)
        }
    }
}