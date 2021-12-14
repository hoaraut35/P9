package com.openclassrooms.realestatemanager.ui.update

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
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
import com.openclassrooms.realestatemanager.utils.CreateUtils
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class UpdateFragmentNew : UpdateAdapter.InterfacePhotoTitleChanged, Fragment() {

    private val viewModelUpdate by viewModels<ViewModelUpdate>()

    private var _binding: FragmentUpdateNewBinding? = null
    private val binding get() = _binding!!

    lateinit var activityResultLauncherForPhoto: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncherForVideo: ActivityResultLauncher<Intent>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateNewBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val recyclerViewMedias: RecyclerView? = binding.recyclerview

        setupActivityResultForCamera()

        //click listener for take photo from camera
        binding.addPhotoCamera?.setOnClickListener {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activityResultLauncherForPhoto.launch(intent)
        }

        //open photo from gallery
        val getImageFromGallery = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->

                if (uri != null) {

                    val bitmap: Bitmap? = MediaStore.Images.Media.getBitmap(
                        context?.applicationContext?.contentResolver, uri
                    )

                    val dateFileName = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                    val fileName = "Photo_"
                    val fileNameDestination = "Photo_" + dateFileName + ".jpg"
                    val fileNameUri: String?
                    fileNameUri = context?.filesDir.toString() + "/"  + "$fileName$dateFileName.jpg"

                    if (bitmap != null) {

                        if (CreateUtils.savePhotoToInternalMemory(
                                requireContext(),
                                fileNameDestination,
                                bitmap
                            )
                        ) {
                            viewModelUpdate.addMediaToList(
                                RealEstateMedia(
                                    realEstateParentId = viewModelUpdate.realEstate.realEstateId,
                                    uri = fileNameUri,
                                    name = ""
                                )
                            )
                        }

                        recyclerViewMedias!!.adapter?.notifyDataSetChanged()
                    }

                }
            }
        )

        //onClickListener to open photo from gallery
        binding.addPhotoFromMemory?.setOnClickListener {
            getImageFromGallery.launch("image/*")
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


        //observe
        viewModelUpdate.getRealEstateFullById()
            .observe(viewLifecycleOwner) { RealEstateFullObserve ->


                binding.edittextPrice?.setText(RealEstateFullObserve.realEstateFullData.price?.toString())
                binding.edittextSurface?.setText(RealEstateFullObserve.realEstateFullData.surface.toString())
                binding.edittextDescription?.setText(RealEstateFullObserve.realEstateFullData.descriptionOfProduct)
                binding.edittextStreetNumber?.setText(RealEstateFullObserve.realEstateFullData.address?.street_number.toString())
                binding.edittextStreetName?.setText(RealEstateFullObserve.realEstateFullData.address?.street_name)
                binding.edittextCityZipcode?.setText(RealEstateFullObserve.realEstateFullData.address?.zip_code.toString())
                binding.edittextCityName?.setText(RealEstateFullObserve.realEstateFullData.address?.city)

                viewModelUpdate.initialListOfMedia =
                    RealEstateFullObserve.mediaList as MutableList<RealEstateMedia>

                if (viewModelUpdate.initialListOfMedia.isNotEmpty() && viewModelUpdate.getMutableListOfMedia()
                        .isEmpty()
                ) {
                    viewModelUpdate.initList()
                }


                //get name of chip selected
                var typeOfProduct: String? = RealEstateFullObserve.realEstateFullData.typeOfProduct

                //    valChipGroupType.setS


                when (RealEstateFullObserve.poi?.station) {
                    true -> binding.stationChip?.isChecked = true
                    false -> binding.stationChip?.isChecked = false
                    else -> binding.stationChip?.isChecked = false
                }

                when (RealEstateFullObserve.poi?.school) {
                    true -> binding.schoolChip?.isChecked = true
                    false -> binding.schoolChip?.isChecked = false
                    else -> binding.schoolChip?.isChecked = false
                }

                when (RealEstateFullObserve.poi?.park) {
                    true -> binding.parcChip?.isChecked = true
                    false -> binding.parcChip?.isChecked = false
                    else -> binding.parcChip?.isChecked = false
                }

                //update actual estate
                viewModelUpdate.realEstate = RealEstateFullObserve.realEstateFullData


                //button listener
                binding.saveBtn?.setOnClickListener {

                    viewModelUpdate.realEstate.typeOfProduct = "House"
                    viewModelUpdate.realEstate.price =
                        binding.edittextPrice?.text.toString().toInt()
                    viewModelUpdate.realEstate.surface =
                        binding.edittextSurface?.text.toString().toInt()
                    viewModelUpdate.realEstate.descriptionOfProduct =
                        binding.edittextDescription?.text.toString()
                    viewModelUpdate.realEstate.address!!.street_number =
                        binding.edittextStreetNumber?.text.toString().toInt()
                    viewModelUpdate.realEstate.address!!.street_name =
                        binding.edittextStreetName?.text.toString()
                    viewModelUpdate.realEstate.address!!.zip_code =
                        binding.edittextCityZipcode?.text.toString().toInt()
                    viewModelUpdate.realEstate.address!!.city =
                        binding.edittextCityName?.text.toString()

                    viewModelUpdate.updateRealEstate(viewModelUpdate.realEstate)

                    for (itemToremove in viewModelUpdate.listOfMediaToRemove) {
                        viewModelUpdate.deleteMedia(itemToremove)
                    }

                    for (item in viewModelUpdate.getMediaListFromVM().value!!) {

                        if (!RealEstateFullObserve.mediaList.contains(item)) {

                            if (!viewModelUpdate.mediaList.contains(item)) {

                                val long = viewModelUpdate.insertMedia(
                                    RealEstateMedia(
                                        uri = item.uri,
                                        realEstateParentId = viewModelUpdate.realEstate.realEstateId,
                                        name = item.name
                                    )
                                )


                            }
                        }


                    }


                    for (media in viewModelUpdate.mediaList) {
                        if (!viewModelUpdate.listOfMediaToRemove.contains(media)) {
                            viewModelUpdate.insertMedia(media)
                        }
                    }


                }


            }

        viewModelUpdate.getMediaListFromVM().observe(viewLifecycleOwner) {
            setupRecyclerView(recyclerViewMedias!!, it)
        }




        updateData()

        return rootView

    }

    private fun updateData() {


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
                        realEstateParentId = viewModelUpdate.realEstate.realEstateId
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
        viewModelUpdate.updateMediaTitle(title, uri)
        viewModelUpdate.setDescriptionTitle(title, uri)
    }

    override fun onDeleteMedia(media: RealEstateMedia) {
        context?.deleteFile(media.uri?.substringAfterLast("/"))
        viewModelUpdate.deleteMedia(media)
        viewModelUpdate.listOfMediaToRemove.add(media)

    }
}