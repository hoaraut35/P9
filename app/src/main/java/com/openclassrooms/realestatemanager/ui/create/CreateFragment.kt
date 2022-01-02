package com.openclassrooms.realestatemanager.ui.create

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstateModifierBinding
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateAddress
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import com.openclassrooms.realestatemanager.models.RealEstatePOI
import com.openclassrooms.realestatemanager.ui.detail.FullScreenFragment
import com.openclassrooms.realestatemanager.ui.update.UpdateUtils
import com.openclassrooms.realestatemanager.utils.SharedUtils
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Field
import java.util.*

@AndroidEntryPoint
class RealEstateModifier : CreateAdapter.InterfacePhotoTitleChanged, Fragment() {

    private var _binding: FragmentRealEstateModifierBinding? = null
    private val binding get() = _binding!!

    private val viewModelCreate by viewModels<CreateViewModel>()

    private lateinit var getPhotoFromCamera: ActivityResultLauncher<Intent>
    private lateinit var getVideoFromCamera: ActivityResultLauncher<Intent>
    private lateinit var getPhotoFromGallery: ActivityResultLauncher<String>
    private lateinit var getVideoFromGallery: ActivityResultLauncher<String>

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRealEstateModifierBinding.inflate(inflater, container, false)
        val rootView = binding.root

        setHasOptionsMenu(true)

        val recyclerView = binding.recyclerview

        val agentSpinner: Spinner = binding.agentsSpinner
        val agent1 = "David"
        val agent2 = "Thierry"
        val agent3 = "Patrick"
        val agentList = listOf(agent1, agent2, agent3)
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            agentList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        agentSpinner.adapter = adapter


        //open media ...
        binding.openMedia.setOnClickListener {
            showPopupMenu(binding.openMedia)
        }

        //get the type of product...

        binding.chipRealEstateType.setOnCheckedChangeListener { group, checkedId ->
            viewModelCreate.realEstateVM.typeOfProduct =
                group.findViewById<Chip>(checkedId)?.text.toString()
        }


        setupGetPhotoFromGallery()
        setupGetPhotoFromCamera()

        //work fine...
        getVideoFromCamera =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
                if (result!!.resultCode == Activity.RESULT_OK) {
                    viewModelCreate.addMediaToList(
                        RealEstateMedia(
                            uri = result.data?.data.toString(),
                            realEstateParentId = 1,
                            name = "video"
                        )
                    )
                }
            }


        //work fine...
        getVideoFromGallery = registerForActivityResult(ActivityResultContracts.GetContent())
        {
            if (it != null) {
                viewModelCreate.addMediaToList(
                    RealEstateMedia(
                        uri = it.toString(),
                        realEstateParentId = 1,
                        name = "video"
                    )
                )
            }
        }

        //save data to database
        saveRealEstateInDB()

        setupViewModel(recyclerView)


        //observe database for notification
        viewModelCreate.observeRowId().observe(viewLifecycleOwner) {

            if (it.toInt() == viewModelCreate.observeRowId().value?.toInt()) {
                SharedUtils.notification(
                    "RealEstate Manager",
                    "Backup complete",
                    requireContext()
                )

                val navHostFragment =
                    requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_item_detail) as NavHostFragment

                val navController = navHostFragment.navController
                navController.navigateUp()

            }


        }

        return rootView
    }


    private fun setupGetPhotoFromGallery() {
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
                        viewModelCreate.addMediaToList(
                            RealEstateMedia(
                                realEstateParentId = 1,
                                uri = fileNameUri,
                                name = "Photo"
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
                        viewModelCreate.addMediaToList(
                            RealEstateMedia(
                                uri = fileNameUri,
                                name = "Photo",
                                realEstateParentId = 1
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

    private fun setupViewModel(recyclerView: RecyclerView) {

        viewModelCreate.getMediasListForUI()
            .observe(viewLifecycleOwner, {

                setupRecyclerView(recyclerView, it)

                val simpleCallback = object :
                    ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                        0
                    ) {

                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder,
                    ): Boolean {

                        val fromPosition = viewHolder.adapterPosition
                        val toPosition = target.adapterPosition

                        Collections.swap(it, fromPosition, toPosition)
                        recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)

                        return true
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    }

                    override fun onSelectedChanged(
                        viewHolder: RecyclerView.ViewHolder?,
                        actionState: Int
                    ) {
                        super.onSelectedChanged(viewHolder, actionState)

                        //start drag
                        when (actionState) {
                            2 -> viewHolder?.itemView?.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.red
                                )

                            )

                            0 -> viewHolder?.itemView?.isVisible = true


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
                itemTouchHelper.attachToRecyclerView(recyclerView)

            })
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


    private fun saveRealEstateInDB() {

        viewModelCreate.getLAstRowId.observe(viewLifecycleOwner) {

            //get last index of realEstate table...
            val lastIndex: Int = it

            binding.saveBtn.setOnClickListener {

                if (testForm().isNullOrEmpty()) {

                    viewModelCreate.insertRealEstate(
                        RealEstate(
                            typeOfProduct = viewModelCreate.realEstateVM.typeOfProduct,
                            dateOfEntry = Utils.getTodayDateToLong(),
                            price = binding.edittextPrice.text.toString().toInt(),
                            surface = binding.edittextSurface.text.toString().toInt(),
                            numberOfRoom = binding.edittextNumberRoom.text.toString().toInt(),
                            numberOfBathRoom = binding.edittextNumberBathroom.text.toString()
                                .toInt(),
                            numberOfBedRoom = binding.edittextNumberBedroom.text.toString()
                                .toInt(),
                            descriptionOfProduct = binding.edittextDescription.text?.toString(),
                            address = RealEstateAddress(
                                street_name = binding.edittextStreetName.text.toString(),
                                street_number = binding.edittextStreetNumber.text.toString()
                                    .toInt(),
                                city = binding.edittextCityName.text.toString(),
                                zip_code = binding.edittextCityZipcode.text.toString().toInt(),
                                country = binding.edittextCountryName.text.toString()
                            ),
                            status = false,
                            agent = binding.agentsSpinner.selectedItem.toString()
                        )
                    )

                    //add medias to database
                    if (!viewModelCreate.getMediasListForUI().value.isNullOrEmpty()) {

                        viewModelCreate.getMediasListForUI().value!!.forEachIndexed { _, realEstateMedia ->
                            viewModelCreate.insertPhoto(
                                RealEstateMedia(
                                    uri = realEstateMedia.uri,
                                    realEstateParentId = lastIndex,
                                    name = realEstateMedia.name
                                )
                            )
                        }


                    }

                    //insert poi
                    var school = false
                    var park = false
                    var gare = false

                    binding.chipRealEstatePoi.checkedChipIds.forEach { myChip ->
                        val chipState =
                            binding.chipRealEstatePoi.findViewById<Chip>(myChip).isChecked
                        when (binding.chipRealEstatePoi.findViewById<Chip>(myChip).text.toString()) {
                            "Ecole" -> school = chipState
                            "Parc" -> park = chipState
                            "Gare" -> gare = chipState
                        }
                    }

                    viewModelCreate.insertPOI(
                        RealEstatePOI(
                            school = school,
                            park = park,
                            station = gare,
                            realEstateParentId = lastIndex
                        )
                    )

                } else {
                    Toast.makeText(requireContext(), testForm(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //setup recyclerView
    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        myRealEstateList: List<RealEstateMedia>
    ) {
        val myLayoutManager = LinearLayoutManager(requireContext())
        myLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = myLayoutManager
        recyclerView.adapter = CreateAdapter(myRealEstateList, this)
    }

    //check data..
    private fun testForm(): String? {

        var messageTest: String? = ""

        if (viewModelCreate.realEstateVM.typeOfProduct.isNullOrEmpty()) {
            messageTest += "You must to select a type of product \r\n"
        }

        if (viewModelCreate.getMediasListForUI().value.isNullOrEmpty()) {
            messageTest += "You minimum one media for the product \r\n"
        }

        if (binding.edittextPrice.text.toString().isEmpty()) {
            messageTest += "You must choice a price \r\n"
        }

        if (binding.edittextSurface.text.toString().isEmpty()) {
            messageTest += "You must choice a surface \r\n"
        }

        if (binding.edittextNumberBathroom.text.toString().isEmpty()) {
            messageTest += "You must choice a number of bathroom \r\n"
        }

        if (binding.edittextNumberBedroom.text.toString().isEmpty()) {
            messageTest += "You must choice a number of Bedroom \r\n"
        }

        if (binding.edittextNumberRoom.text.toString().isEmpty()) {
            messageTest += "You must choice a number of room \r\n"
        }

        if (binding.edittextDescription.text.toString().isEmpty()) {
            messageTest += "You must enter a description \r\n"
        }

        if (binding.edittextStreetNumber.text.toString().isEmpty()) {
            messageTest += "You must enter a street number \r\n"
        }

        if (binding.edittextStreetName.text.toString().isEmpty()){
            messageTest += "You must enter a street name \r\n"
        }

        if (binding.edittextCityName.text.toString().isEmpty()){
            messageTest += "You must enter a city name \r\n"
        }

        if (binding.edittextCityZipcode.text.toString().isEmpty()){
            messageTest += "You must enter a zip code \r\n"
        }

        if (binding.edittextCountryName.text.toString().isEmpty()){
            messageTest += "You must enter a country name \r\n"
        }

        return messageTest

    }


    //setup menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.realEstateUpdateBtnNew).isVisible = false
        menu.findItem(R.id.realEstateUpdateBtnNew).isVisible = false
    }

    override fun onViewFullScreenMedia(title: String, uri: String) {
        val args = Bundle()
        args.putString("uri", uri)
        val dialog = FullScreenFragment()
        dialog.arguments = args
        dialog.show(childFragmentManager, "test")
    }

    //callBack recyclerView
    override fun onChangedTitleMedia(title: String, uri: String) {
        viewModelCreate.updateMediaTitle(title, uri)
    }

    //callBack recyclerView
    override fun onDeletePhoto(media: RealEstateMedia) {
        viewModelCreate.deleteMedia(media)
        context?.deleteFile(media.uri?.substringAfterLast("/"))
    }


}