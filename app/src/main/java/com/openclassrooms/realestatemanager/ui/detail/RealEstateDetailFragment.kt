package com.openclassrooms.realestatemanager.ui.detail

import android.app.Activity.MODE_PRIVATE
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
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
import com.openclassrooms.realestatemanager.ui.updatenew.ViewModelUpdate
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_REAL_ESTATE_ID = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint  //Hilt annotation for fragment
class RealEstateDetailFragment : Fragment(), MyRequestImageListener.Callback {

    private var itemIdBundle: String? = null

    var adresse: String? = null
    private val REQUEST_IMAGE_CAPTURE = 1

    //binding
    private var _binding: FragmentRealEstateDetailBinding? = null
    private val binding get() = _binding!!

    lateinit var imageMap: ImageView
    lateinit var imageMapCopy: ImageView

    lateinit var imageUri2: String

    //viewmodel
    private val mainViewModel by viewModels<MainViewModel>()
    //private val updateViewModel by viewModels<ViewModelUpdate>()
    private val detailViewModel by viewModels<ViewModelDetail>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_REAL_ESTATE_ID)) {
                itemIdBundle = it.getString(ARG_REAL_ESTATE_ID)

                //updateViewModel.loadRealEstateId(item_id_bundle.toString())
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

        val recyclerViewPhotos: RecyclerView? = binding.recyclerviewPhotos

        setHasOptionsMenu(true);

        imageMapCopy = binding.imageMap!!
        imageMap = binding.imageMapTest!!

        mainViewModel.allRealEstateWithPhotos.observe(viewLifecycleOwner) { it ->

            val realEstate: RealEstateWithMedia? =
                it.find {
                    it.realEstate.realEstateId.toString() == itemIdBundle
                }

            //bundle
            if (realEstate != null) {

                detailViewModel.myRealEstate = realEstate.realEstate

                //decription
                binding.textDescriptionDetail.setText(realEstate.realEstate.descriptionOfProduct)

                //address
                binding.textNumberStreet?.setText(realEstate.realEstate.address?.street_number.toString())
                binding.textStreetName?.setText(realEstate.realEstate.address?.street_name.toString())
                binding.textCityName?.setText(realEstate.realEstate.address?.city.toString())
                binding.textZipcode?.setText(realEstate.realEstate.address?.zip_code.toString())
                binding.textCountry?.setText(realEstate.realEstate.address?.country.toString())

                binding.textPrice?.setText(realEstate.realEstate.price.toString())
                binding.qtySurface?.setText(realEstate.realEstate.surface.toString())

                //data
                binding.qtyNumberBedroom.setText("Bedroom : " + realEstate.realEstate.numberOfBedRoom.toString())
                binding.qtyNumberBathroom.setText("Bathroom : " + realEstate.realEstate.numberOfBathRoom.toString())
                binding.qtyNumberRoom.setText("Room : " + realEstate.realEstate.numberOfRoom.toString())


                var address: String = realEstate.realEstate.address?.city.toString()
                recyclerViewPhotos?.let { setupRecyclerView(it, realEstate.mediaList) }

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

    //take a photo of property
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras!!.get("data") as Bitmap
            binding.staticMapView!!.setImageBitmap(imageBitmap)

            val fileName: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir = File(context?.filesDir, "test")

            //   val photoUri : Uri = FileProvider.getUriForFile(this, "com.openclassrooms.realestatemanager",storageDir)

            //File.createTempFile("JPEG_THOMAS_",".jpg",context?.filesDir).apply { Log.i("[THOMAS]", "Photo path :$absolutePath"  ) }

            if (savePhotoToInternalMemory("Photo_$fileName", imageBitmap)) {
                Log.i("[TEST]", "Vrai")
            } else {
                Log.i("[TEST]", "Faux")
            }

        }
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

            //bitmap
            val staticMapBitmap = dataSource.toBitmap()

            //t show for
            Glide.with(this)
                .load(staticMapBitmap)
                .centerCrop()
                .into(imageMapCopy)

            val fileName: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

            if (savePhotoToInternalMemory("Photo_$fileName", staticMapBitmap)) {
                Log.i("[TEST]", "Vrai")
            } else {
                Log.i("[TEST]", "Faux")
            }
            //savePhotoToInternalMemory(fileName, staticMapBitmap)

        }
    }
}