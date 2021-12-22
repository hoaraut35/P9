package com.openclassrooms.realestatemanager.ui.update

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentUpdateNewBinding
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Field
import java.util.*

@AndroidEntryPoint
class UpdateFragmentNew : UpdateAdapter.InterfaceMediaAdapter, Fragment() {

    private val viewModelUpdate by viewModels<ViewModelUpdate>()

    private var _binding: FragmentUpdateNewBinding? = null
    private val binding get() = _binding!!

    private lateinit var getPhotoFromCamera: ActivityResultLauncher<Intent>
    private lateinit var getVideoFromCamera: ActivityResultLauncher<Intent>
    private lateinit var getPhotoFromGallery: ActivityResultLauncher<String>
    private lateinit var getVideoFromGallery: ActivityResultLauncher<String>

    private var dateOfSold: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateNewBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val recyclerViewMedias: RecyclerView? = binding.recyclerview

        //open a media...
        binding.addMediaBtn?.setOnClickListener {
            showPopupMenu(binding.addMediaBtn!!)
        }

        //open a photo from gallery...
        setupGetPhotoFromGallery(recyclerViewMedias!!)

        //open a video from camera...
        setupGetVideoFromCamera(recyclerViewMedias)

        //open a video from gallery...
        setupGetVideoFromGallery(recyclerViewMedias)

        //open a photo from camera...
        setupGetPhotoFromCamera()

        //observe updated realEstate...
        viewModelUpdate.getRealEstateFullById()
            .observe(viewLifecycleOwner) { RealEstateFullObserve ->

                binding.edittextPrice?.setText(RealEstateFullObserve.realEstateFullData.price?.toString())
                binding.edittextSurface?.setText(RealEstateFullObserve.realEstateFullData.surface.toString())
                binding.edittextDescription?.setText(RealEstateFullObserve.realEstateFullData.descriptionOfProduct)
                binding.edittextStreetNumber?.setText(RealEstateFullObserve.realEstateFullData.address?.street_number.toString())
                binding.edittextStreetName?.setText(RealEstateFullObserve.realEstateFullData.address?.street_name)
                binding.edittextCityZipcode?.setText(RealEstateFullObserve.realEstateFullData.address?.zip_code.toString())
                binding.edittextCityName?.setText(RealEstateFullObserve.realEstateFullData.address?.city)

                binding.isSoldSwitch?.isChecked =
                    RealEstateFullObserve.realEstateFullData.releaseDate != null

                //type of product...
                when (RealEstateFullObserve.realEstateFullData.typeOfProduct) {
                    "House" -> binding.chipHouse?.isChecked = true
                    "Flat" -> binding.chipFlat?.isChecked = true
                    "Duplex" -> binding.chipDuplex?.isChecked = true
                }

                //point of interest...
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


                //save the initial list of media in viewModel
                viewModelUpdate.initialListOfMedia =
                    RealEstateFullObserve.mediaList as MutableList<RealEstateMedia>

                //if the we don't have modified list then show initial list...
                if (viewModelUpdate.initialListOfMedia.isNotEmpty() && viewModelUpdate.getMutableListOfMedia()
                        .isEmpty()
                ) {
                    Log.i("[UPMEDIA]", "Initial list already updated load it ... : ")
                    viewModelUpdate.initList()
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


                    //Update the realEsatte ....
                    viewModelUpdate.realEstate.releaseDate = dateOfSold
                    viewModelUpdate.updateRealEstate(viewModelUpdate.realEstate)



                    if (!viewModelUpdate.listOfMediaToRemove.isNullOrEmpty()) {
                        for (itemToRemove in viewModelUpdate.listOfMediaToRemove) {
                            //    viewModelUpdate.deleteMedia(itemToRemove)

                            //    viewModelUpdate.deleteMediaFromDatabase(itemToRemove)
                            //move to save btn
                            UpdateUtils.deleteMediaFromInternalMemory(
                                requireContext(),
                                itemToRemove
                            )


                            //context?.deleteFile(mediaToDelete.uri.substringAfterLast("/"))
                            //viewModel.deletePropertyMediaFromDb(mediaToDelete)
                        }
                    }

                    //to add
                    if (viewModelUpdate.getMediaListFromVM().value != null) {
                        for (item in viewModelUpdate.getMediaListFromVM().value!!) {
                            if (!RealEstateFullObserve.mediaList.contains(item)) {
                                if (!viewModelUpdate.mediaList.contains(item)) {
                                    val long = viewModelUpdate.insertMedia(
                                        RealEstateMedia(
                                            uri = item.uri,
                                            realEstateParentId = RealEstateFullObserve.realEstateFullData.realEstateId,
                                            name = item.name

                                        )
                                    )
                                }
                            }
                        }
                    }


                    //for title
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

            setupRecyclerView(recyclerViewMedias, myMedia)

            val simpleCallback = object :
                ItemTouchHelper.SimpleCallback(
                    ItemTouchHelper.START or ItemTouchHelper.END,
                    0
                ) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder,
                ): Boolean {

                    val adapter = (binding.recyclerview?.adapter as UpdateAdapter).mediaList

                    val fromPosition = viewHolder.adapterPosition
                    val toPosition = target.adapterPosition

                    Collections.swap(adapter, fromPosition, toPosition)
                    recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
                    viewModelUpdate.newListOfMedia = adapter as MutableList<RealEstateMedia>

                    return true
                }

                override fun onSelectedChanged(
                    viewHolder: RecyclerView.ViewHolder?,
                    actionState: Int
                ) {
                    super.onSelectedChanged(viewHolder, actionState)

                    if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                        viewHolder?.itemView?.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.myColorItem
                            )
                        )
                    }

                }

                override fun clearView(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ) {
                    super.clearView(recyclerView, viewHolder)
                    viewHolder.itemView.setBackgroundColor(0)
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                }

            }

            val itemTouchHelper = ItemTouchHelper(simpleCallback)
            itemTouchHelper.attachToRecyclerView(binding.recyclerview)

        }

        //sold button...
        binding.isSoldSwitch?.setOnClickListener {
            dateOfSold = when {
                binding.isSoldSwitch!!.isChecked -> {
                    Utils.getTodayDateToLong()
                }
                else -> {
                    null
                }
            }
        }

        return rootView

    }

    private fun setupGetPhotoFromGallery(recyclerView: RecyclerView) {
        getPhotoFromGallery = registerForActivityResult(
            ActivityResultContracts.GetContent()
        )
        { uri ->

            if (uri != null) {

                val bitmap: Bitmap? = MediaStore.Images.Media.getBitmap(
                    context?.applicationContext?.contentResolver, uri
                )

                val dateFileName: String = UpdateUtils.getTodayDate().toString()
                val fileName = "Photo_"
                val fileNameDestination = "$fileName$dateFileName.jpg"
                val fileNameUri: String?
                fileNameUri = context?.filesDir.toString() + "/" + "$fileName$dateFileName.jpg"

                if (bitmap != null) {

                    if (UpdateUtils.savePhotoToInternalMemory(
                            fileNameDestination,
                            bitmap,
                            requireContext()
                        )
                    ) {
                        viewModelUpdate.addMediaToList(
                            RealEstateMedia(
                                realEstateParentId = viewModelUpdate.realEstate.realEstateId,
                                uri = fileNameUri,
                                name = "Photo"
                            )
                        )
                    }else {
                        Toast.makeText(
                            requireContext(),
                            "Fail to save photo to internal memory",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }

            }
        }
    }

    private fun setupGetPhotoFromCamera() {
        getPhotoFromCamera =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->

                if (result?.resultCode == Activity.RESULT_OK) {

                    val bitmap = result.data!!.extras!!.get("data") as Bitmap

                    val dateFileName: String = UpdateUtils.getTodayDate().toString()
                    val fileName = "Photo_"
                    val fileNameDestination = "$fileName$dateFileName.jpg"
                    val fileNameUri: String?
                    fileNameUri = context?.filesDir.toString() + "/" + "$fileName$dateFileName.jpg"

                    if (UpdateUtils.savePhotoToInternalMemory(
                            fileNameDestination,
                            bitmap,
                            requireContext()
                        )
                    ) {
                        viewModelUpdate.addMediaToList(
                            RealEstateMedia(
                                uri = fileNameUri,
                                name = "Photo",
                                realEstateParentId = viewModelUpdate.realEstate.realEstateId
                            )
                        )
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Fail to save photo to internal memory",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    private fun setupGetVideoFromGallery(recyclerViewMedias: RecyclerView) {
        getVideoFromGallery = registerForActivityResult(
            ActivityResultContracts.GetContent()
        )
        {
            if (it != null) {

                val dateFileName: String = UpdateUtils.getTodayDate().toString()
                val fileName = "Video_"
                val fileNameDestination = "$fileName$dateFileName.mp4"

                if (UpdateUtils.saveVideoToInternalStorage(
                        fileNameDestination,
                        it,
                        requireContext()
                    )
                ) {
                    val fileNameUri = context?.filesDir.toString() + "/" + fileNameDestination

                    viewModelUpdate.addMediaToList(
                        RealEstateMedia(
                            uri = fileNameUri,
                            realEstateParentId = viewModelUpdate.realEstate.realEstateId,
                            name = "Video"
                        )
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Fail to save video to internal memory",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupGetVideoFromCamera(recyclerView: RecyclerView) {
        getVideoFromCamera =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->

                if (result!!.resultCode == Activity.RESULT_OK) {


                    val dateFileName: String = UpdateUtils.getTodayDate().toString()
                    val fileName = "Video_"
                    val fileNameDestination = "$fileName$dateFileName.mp4"

                    if (UpdateUtils.saveVideoToInternalStorage(
                            fileNameDestination,
                            result.data?.data.toString().toUri(),
                            requireContext()
                        )
                    ) {
                        val fileNameUri = context?.filesDir.toString() + "/" + fileNameDestination
                        viewModelUpdate.addMediaToList(
                            RealEstateMedia(
                                uri = fileNameUri,
                                realEstateParentId = viewModelUpdate.realEstate.realEstateId,
                                name = "Video"
                            )
                        )
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Fail to save video to internal memory",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    @SuppressLint("DiscouragedPrivateApi")
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.popup)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                //ok
                R.id.photoFromGallery -> {
                    getPhotoFromGallery.launch("image/*")
                    true
                }
                R.id.photoFromCamera -> {
                    getPhotoFromCamera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                    true
                }

                R.id.videoFromCamera -> {
                    val intentVideo = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                    getVideoFromCamera.launch(intentVideo)
                    true
                }

                R.id.videoFromGallery -> {
                    getVideoFromGallery.launch("video/*")
                    true
                }

                else -> true
            }
        }

        //to show icon in popup menu
        val popup: Field = PopupMenu::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val menu: Any? = popup.get(popupMenu)
        menu?.javaClass?.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
            ?.invoke(menu, true)
        popupMenu.show()

    }


    //setup recyclerview...
    private fun setupRecyclerView(recyclerView: RecyclerView, mediaList: List<RealEstateMedia>) {
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = UpdateAdapter(mediaList, this)
    }

    //callback adapter...
    override fun onChangedTitleMedia(title: String, uri: String) {
        viewModelUpdate.updateMediaTitle(title, uri)
    }

    //callback adapter...
    override fun onDeleteMedia(media: RealEstateMedia) {
        viewModelUpdate.deleteMedia(media)
        viewModelUpdate.deleteMediaFromDatabase(media)
        viewModelUpdate.listOfMediaToRemove.add(media)
    }

    //callback adapter...
    override fun onToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

}