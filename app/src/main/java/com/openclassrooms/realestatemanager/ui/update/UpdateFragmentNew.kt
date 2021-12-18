package com.openclassrooms.realestatemanager.ui.update

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentUpdateNewBinding
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import com.openclassrooms.realestatemanager.utils.CreateUtils
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class UpdateFragmentNew : UpdateAdapter.InterfaceMediaAdapter, Fragment() {

    private val viewModelUpdate by viewModels<ViewModelUpdate>()

    private var _binding: FragmentUpdateNewBinding? = null
    private val binding get() = _binding!!

    private lateinit var activityResultLauncherForPhoto: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncherForVideo: ActivityResultLauncher<Intent>

    private var newList = mutableListOf<RealEstateMedia>()
    private var dateOfSold: Long? = null

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
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
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
                    val fileNameDestination = "Photo_$dateFileName.jpg"
                    val fileNameUri: String?
                    fileNameUri = context?.filesDir.toString() + "/" + "$fileName$dateFileName.jpg"

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
                                    name = "Photo"
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
                            name = "Video"
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


                //**********************************************************************************


                binding.edittextPrice?.setText(RealEstateFullObserve.realEstateFullData.price?.toString())
                binding.edittextSurface?.setText(RealEstateFullObserve.realEstateFullData.surface.toString())
                binding.edittextDescription?.setText(RealEstateFullObserve.realEstateFullData.descriptionOfProduct)
                binding.edittextStreetNumber?.setText(RealEstateFullObserve.realEstateFullData.address?.street_number.toString())
                binding.edittextStreetName?.setText(RealEstateFullObserve.realEstateFullData.address?.street_name)
                binding.edittextCityZipcode?.setText(RealEstateFullObserve.realEstateFullData.address?.zip_code.toString())
                binding.edittextCityName?.setText(RealEstateFullObserve.realEstateFullData.address?.city)




                binding.isSoldSwitch?.isChecked =
                    RealEstateFullObserve.realEstateFullData.releaseDate != null


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


                    viewModelUpdate.realEstate.releaseDate = dateOfSold

                    viewModelUpdate.updateRealEstate(viewModelUpdate.realEstate)

                    for (itemToremove in viewModelUpdate.listOfMediaToRemove) {
                        viewModelUpdate.deleteMedia(itemToremove)
                    }


//                    for (item in viewModelUpdate.getMediaListFromVM().value!!) {
//
//                        if (!RealEstateFullObserve.mediaList.contains(item)) {
//
//                            if (!viewModelUpdate.mediaList.contains(item)) {
//
//                                val long = viewModelUpdate.insertMedia(
//                                    RealEstateMedia(
//                                        uri = item.uri,
//                                        realEstateParentId = viewModelUpdate.realEstate.realEstateId,
//                                        name = item.name,
//                                        position = viewModelUpdate.realEstate.realEstateId
//                                    )
//                                )
//
//
//                            }
//                        }
//
//
//                    }


                    for (item in newList) {

                        if (!RealEstateFullObserve.mediaList.contains(item)) {
                            if (!viewModelUpdate.mediaList.contains(item)) {
                                val long = viewModelUpdate.insertMedia(
                                    RealEstateMedia(
                                        uri = item.uri,
                                        realEstateParentId = viewModelUpdate.realEstate.realEstateId,
                                        name = item.name,
                                        position = item.position
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


                    //notification("RealEsatzte", "Update terminé")
                    val navHostFragment =
                        requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_item_detail) as NavHostFragment

                    val navController = navHostFragment.navController
                    navController.navigateUp()


                }


            }

        viewModelUpdate.getMediaListFromVM().observe(viewLifecycleOwner) { myMedia ->


            setupRecyclerView(recyclerViewMedias!!, myMedia.sortedBy { it.position })


            val simpleCallback = object :
                ItemTouchHelper.SimpleCallback(
                    ItemTouchHelper.START or ItemTouchHelper.END,
                    0
                ) {


                override fun onMoved(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    fromPos: Int,
                    target: RecyclerView.ViewHolder,
                    toPos: Int,
                    x: Int,
                    y: Int
                ) {
                    super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)

//                        recyclerView.adapter?.registerAdapterDataObserver{
//
//                        }


                    val adapterList = (binding.recyclerview?.adapter as UpdateAdapter).mediaList


                    for (i in adapterList.indices) {
                        adapterList[i].name?.let { Log.i("[ADAPTER]", it) }
                            adapterList[i].position = i
                    }


                    Log.i("[ADAPTER]", "NEW:" + adapterList.toString())


                }


                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder,
                ): Boolean {
                    val fromPosition = viewHolder.adapterPosition
                    val toPosition = target.adapterPosition
                    Collections.swap(myMedia, fromPosition, toPosition)

                    recyclerView.adapter!!.notifyItemMoved(fromPosition, toPosition)

//                    val adapterList = (binding.recyclerview?.adapter as UpdateAdapter).mediaList
//
//                    Log.i("[ADAPTER]","ORIGIN:" + adapterList.toString())


                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    Log.i("[ADAPTER]","ORIGIN:")
                }


                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )


                    if (actionState == ItemTouchHelper.ACTION_STATE_DRAG){
                        viewHolder.itemView.setBackgroundColor(Color.parseColor("#FFEB3B"))
                    }else
                    {
                        viewHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    }

                }

                override fun onSelectedChanged(
                    viewHolder: RecyclerView.ViewHolder?,
                    actionState: Int
                ) {
                    super.onSelectedChanged(viewHolder, actionState)





                    if (actionState == 0) {

//                        val adapterList = (binding.recyclerview?.adapter as UpdateAdapter).mediaList
//
//                        for (i in adapterList.indices) {
//                            adapterList[i].position = i
//                        }
//
//                        //newList = adapterList
//                        Log.i("[ADAPTER]","NEW :" + adapterList.toString())
                    }


                    //start drag
                    when (actionState) {


                        8 -> viewHolder?.itemView?.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.design_default_color_on_secondary
                            )
                        )


                    }


                }


            }

            val itemTouchHelper = ItemTouchHelper(simpleCallback)
            itemTouchHelper.attachToRecyclerView(binding.recyclerview)


        }



        binding.isSoldSwitch?.setOnClickListener {
            if (binding.isSoldSwitch!!.isChecked) {
                dateOfSold = Utils.getTodayDateToLong()
            } else {
                dateOfSold = null
            }
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

                    var bitmap = result.data!!.extras!!.get("data") as Bitmap

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
                        uri = fileNameUri,
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