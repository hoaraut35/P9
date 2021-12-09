package com.openclassrooms.realestatemanager.ui.updatenew

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.databinding.FragmentUpdateNewBinding
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class UpdateFragmentNew : UpdateAdapter.InterfacePhotoTitleChanged, Fragment() {

    private val viewModelUpdate by viewModels<ViewModelUpdate>()

    //binding
    private var _binding: FragmentUpdateNewBinding? = null
    private val binding get() = _binding!!

    lateinit var activityResultLauncherForPhoto: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncherForVideo: ActivityResultLauncher<Intent>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUpdateNewBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val recyclerViewMedias: RecyclerView? = binding.recyclerview


        //setup callback for camera
        setupActivityResultForCamera()

        //click listener for take photo from camera
        binding.addPhotoCamera?.setOnClickListener {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activityResultLauncherForPhoto.launch(intent)
        }

        //launcher for open video from gallery
        val getImageFromGallery = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {

                //to show image
                // binding.imageOfGallery?.setImageURI(it)

                if (it != null) {

                    val bitmap: Bitmap? = MediaStore.Images.Media.getBitmap(
                        context?.applicationContext?.contentResolver, it
                    )

                    val fileName2: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

                    if (bitmap != null) {
                        savePhotoToInternalMemory("Photo_$fileName2", bitmap)
                        recyclerViewMedias!!.adapter?.notifyDataSetChanged()
                    }

                }
            }
        )

        //listener for button click gallery pick
        binding.addPhotoFromMemory?.setOnClickListener {
            getImageFromGallery.launch("image/*")
            // setupGetPhotoFromGalery()
            Log.i("[THOMAS]", "test ouverture photo")
        }


        //get result for a video from the camera
        activityResultLauncherForVideo =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
                if (result!!.resultCode == Activity.RESULT_OK) {
                    viewModelUpdate.addMediaToList(
                        RealEstateMedia(
                            uri = result.data?.data.toString(),
                            realEstateParentId = 1,
                            name = "video"
                        )
                    )

                    recyclerViewMedias!!.adapter?.notifyDataSetChanged()
                }
            }


        //get result for a video from gallery
        val getVideoFromGallery = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {

                if (it != null) {
                    viewModelUpdate.addMediaToList(
                        RealEstateMedia(
                            uri = it.toString(),
                            realEstateParentId = 1,
                            name = "video"
                        )
                    )

                    recyclerViewMedias!!.adapter?.notifyDataSetChanged()
                }
            }
        )


        //click listener to get video from camera
        binding.addVideoFromCamera?.setOnClickListener {
            val intentVideo = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            activityResultLauncherForVideo.launch(intentVideo)
        }


        //click listener for open video from gallery
        binding.addVideoFromMemory?.setOnClickListener {
            getVideoFromGallery.launch("video/*")
        }





        viewModelUpdate.getRealEstateFullById().observe(viewLifecycleOwner) {

            binding.edittextPrice?.setText(it.realEstateFullData.price?.toString())
            binding.edittextSurface?.setText(it.realEstateFullData.surface.toString())
            binding.edittextDescription?.setText(it.realEstateFullData.descriptionOfProduct)
            binding.edittextStreetNumber?.setText(it.realEstateFullData.address?.street_number.toString())
            binding.edittextStreetName?.setText(it.realEstateFullData.address?.street_name)
            binding.edittextCityZipcode?.setText(it.realEstateFullData.address?.zip_code.toString())
            binding.edittextCityName?.setText(it.realEstateFullData.address?.city)




            viewModelUpdate.initialListOfMedia = it.mediaList as MutableList<RealEstateMedia>

            if (viewModelUpdate.initialListOfMedia.isNotEmpty() && viewModelUpdate.getMutableListOfMedia()
                    .isEmpty()
            ) {
                viewModelUpdate.initList()
            }


            //get name of chip selected
            var typeOfProduct: String? = it.realEstateFullData.typeOfProduct

            //    valChipGroupType.setS


            when (it.poi?.station) {
                true -> binding.stationChip?.isChecked = true
                false -> binding.stationChip?.isChecked = false
                else -> binding.stationChip?.isChecked = false
            }

            when (it.poi?.school) {
                true -> binding.schoolChip?.isChecked = true
                false -> binding.schoolChip?.isChecked = false
                else -> binding.schoolChip?.isChecked = false
            }

            when (it.poi?.park) {
                true -> binding.parcChip?.isChecked = true
                false -> binding.parcChip?.isChecked = false
                else -> binding.parcChip?.isChecked = false
            }

            //update actual estate
            viewModelUpdate.realEstate = it.realEstateFullData

        }

        viewModelUpdate.getUIToShow().observe(viewLifecycleOwner) {
            setupRecyclerView(recyclerViewMedias!!, it)
        }


//        //launcher for open video from gallery
//        val getImageFromGallery = registerForActivityResult(
//            ActivityResultContracts.GetContent(),
//            ActivityResultCallback {
//
//                //to show image
//               // binding.imageOfGallery?.setImageURI(it)
//
//                if (it != null) {
//
//                    val bitmap: Bitmap? = MediaStore.Images.Media.getBitmap(
//                        context?.applicationContext?.contentResolver, it
//                    )
//
//                    val fileName2: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//
//                    if (bitmap != null) {
//                        savePhotoToInternalMemory("Photo_$fileName2", bitmap)
//                        recyclerViewMedias.adapter?.notifyDataSetChanged()
//                    }
//
//                }
//            }
//        )

        //listener for button click gallery pick
//        binding.addPhotoFromMemory?.setOnClickListener {
//            getImageFromGallery.launch("image/*")
//            // setupGetPhotoFromGalery()
//            Log.i("[THOMAS]", "test ouverture photo")
//        }



        binding.saveBtn?.setOnClickListener{
            updateData()
        }

        return rootView

    }

    private fun updateData() {

        viewModelUpdate.realEstate.price = 10
        viewModelUpdate.realEstate.descriptionOfProduct = "zz"
        viewModelUpdate.realEstate.typeOfProduct = "Cabane"
//        viewModelUpdate.realEstate
//        viewModelUpdate.realEstate
//        viewModelUpdate.realEstate
//
        viewModelUpdate.updateRealEstate(viewModelUpdate.realEstate)


    }


    //TODO: move to util class ?
    private fun setupActivityResultForCamera() {
        activityResultLauncherForPhoto =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->

                if (result?.resultCode == Activity.RESULT_OK) {

                    var bitmap = result!!.data!!.extras!!.get("data") as Bitmap

                    val fileName: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                    //val storageDir = File(context?.filesDir, "test")
                    savePhotoToInternalMemory("Photo_$fileName", bitmap)
                }

            }

    }


    //TODO: move to util class?
    private fun savePhotoToInternalMemory(filename: String, bmp: Bitmap): Boolean {
        return try {
            context?.openFileOutput("$filename.jpg", Activity.MODE_PRIVATE).use { stream ->

                //compress photo
                if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                    throw IOException("erreur compression")
                }

                val fileNameUri = context?.filesDir.toString() + "/" + filename + ".jpg"


                //add in viewmodel list
                viewModelUpdate.addMediaToList(
                    RealEstateMedia(
                        uri = fileNameUri!!,
                        name = "test",
                        realEstateParentId = 1
                    )
                )

            }
            true

        } catch (e: IOException) {
            e.printStackTrace()
            false

        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, mediaList: List<RealEstateMedia>) {
        val myLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = myLayoutManager
        recyclerView.adapter = UpdateAdapter(mediaList, this)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UpdateFragmentNew().apply {

            }
    }

    override fun onChangedTitlePhoto(title: String, uri: String) {
        // viewModelUpdate.set
    }

    override fun onDeleteMedia(media: RealEstateMedia) {
        viewModelUpdate.deleteMedia(media)
    }
}