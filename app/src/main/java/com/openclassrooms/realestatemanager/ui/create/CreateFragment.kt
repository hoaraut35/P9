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
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
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
import com.openclassrooms.realestatemanager.ui.MainViewModel
import com.openclassrooms.realestatemanager.ui.detail.FullScreenFragment
import com.openclassrooms.realestatemanager.utils.SharedUtils
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.lang.reflect.Field
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class RealEstateModifier : CreateAdapter.InterfacePhotoTitleChanged, Fragment() {

    private var _binding: FragmentRealEstateModifierBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by viewModels<MainViewModel>()
    private val viewModelCreate by viewModels<CreateViewModel>()

    private val listOfMediasToSave = mutableListOf<RealEstateMedia>()

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

        val agentSpinner: Spinner? = binding.agentsSpinner
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
        agentSpinner?.adapter = adapter


        //open media ...
        binding.openMedia.setOnClickListener {
            showPopupMenu(binding.openMedia)
        }

        //get the type of product...

        binding.chipRealEstateType.setOnCheckedChangeListener { group, checkedId ->
            viewModelCreate.realEstateVM.typeOfProduct =
                group.findViewById<Chip>(checkedId)?.text.toString()
        }


        //binding.chipRealEstatePoi.setOnCheckedChangeListener { group, checkedId ->
        //  viewModelCreate.propertyTypeChanged(group.findViewById<Chip>(checkedId)?.text.toString())
        //}


        binding.edittextPrice?.addTextChangedListener {
            binding.propertyPriceText.helperText =
                SharedUtils.validPriceText(binding.edittextPrice!!.text)
        }


        //setupActionAfterGetImageFromGallery()

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





        //work fine...
        getPhotoFromGallery = registerForActivityResult(ActivityResultContracts.GetContent())
        { uri ->

            if (uri != null) {

                val bitmap: Bitmap? = MediaStore.Images.Media.getBitmap(
                    context?.applicationContext?.contentResolver, uri
                )

                val dateFileName = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val fileName = "Photo_"
                val fileNameDestination = "$fileName$dateFileName.jpg"

                //set uri
                val fileNameUri: String?
                fileNameUri = context?.filesDir.toString() + "/" + fileNameDestination + ".jpg"

                //if we have a bitmap then ...
                if (bitmap != null) {
                    savePhotoToInternalMemory("$fileName$dateFileName", bitmap)
                    // recyclerView!!.adapter?.notifyDataSetChanged()
                }

            }
        }

        getPhotoFromCamera =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->

                if (result?.resultCode == Activity.RESULT_OK) {

                    val bitmap = result.data!!.extras!!.get("data") as Bitmap
                    val dateFileName: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

                    val fileName: String = "Photo_"

                    //val storageDir = File(context?.filesDir, "test")

                    savePhotoToInternalMemory("$dateFileName", bitmap)

                }

            }



        setupActivityResultForCamera()
        //setupActivityResultForGallery()


        //save data to database
        saveRealEstateInDB()

        setupViewModel(recyclerView)

        binding.edittextPrice?.addTextChangedListener {
            binding.propertyPriceText.helperText =
                SharedUtils.validPriceText(binding.edittextPrice!!.text)
        }

        binding.edittextPrice?.setOnFocusChangeListener { _, focused ->
            binding.propertyPriceText.helperText =
                SharedUtils.validPriceText(binding.edittextPrice!!.text)
        }

        binding.edittextDescription?.addTextChangedListener {
            binding.propertyDescriptionText.helperText =
                SharedUtils.validPriceText(binding.edittextDescription!!.text)
        }

        binding.edittextDescription?.setOnFocusChangeListener { _, _ ->
            binding.propertyDescriptionText.helperText =
                SharedUtils.validPriceText(binding.edittextDescription!!.text)
        }


        //observe database for notification
        viewModelCreate.observeRowId().observe(viewLifecycleOwner) {

            if (it.toInt() == viewModelCreate.observeRowId().value?.toInt()) {
                SharedUtils.notification(
                    "RealEstate Manager",
                    "Sauvegarde terminée",
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
                viewModelCreate.addMediaToList(
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

        mainViewModel.getLAstRowId.observe(viewLifecycleOwner) {

            //get last index of realEstate table...
            val lastIndex: Int = it

            binding.saveBtn.setOnClickListener {

                if (testForm().isNullOrEmpty()) {

                    viewModelCreate.insertRealEstate(
                        RealEstate(
                            typeOfProduct = viewModelCreate.realEstateVM.typeOfProduct,
                            dateOfEntry = Utils.getTodayDateToLong(),
                            price = binding.edittextPrice?.text.toString().toInt(),
                            surface = binding.edittextSurface?.text.toString().toInt(),
                            numberOfRoom = binding.edittextNumberRoom?.text.toString().toInt(),
                            numberOfBathRoom = binding.edittextNumberBathroom?.text.toString()
                                .toInt(),
                            numberOfBedRoom = binding.edittextNumberBedroom?.text.toString()
                                .toInt(),
                            descriptionOfProduct = binding.edittextDescription?.text?.toString(),
                            address = RealEstateAddress(
                                street_name = binding.edittextStreetName?.text.toString(),
                                street_number = binding.edittextStreetNumber?.text.toString()
                                    .toInt(),
                                city = binding.edittextCityName?.text.toString(),
                                zip_code = binding.edittextCityZipcode?.text.toString().toInt(),
                                country = null
                            ),
                            status = false,
                            agent = binding.agentsSpinner?.selectedItem.toString()
                        )
                    )


                    //add medias to database
                    if (!viewModelCreate.getMediasListForUI().value.isNullOrEmpty()) {

                        viewModelCreate.getMediasListForUI().value!!.forEachIndexed { index, realEstateMedia ->
                            mainViewModel.insertPhoto(
                                RealEstateMedia(
                                    uri = realEstateMedia.uri,
                                    realEstateParentId = lastIndex,
                                    name = realEstateMedia.name
                                    //position = index
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

    //TODO: move to utils
    private fun setupActivityResultForGallery() {
        //to get image from gallery
        val getImageFromGallery = registerForActivityResult(
            ActivityResultContracts.GetContent(),

            ActivityResultCallback {
                //      binding.imageOfGallery?.setImageURI(it)
                val fileName: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                //val storageDir = File(context?.filesDir, "test")
                //savePhotoToInternalMemory("Photo_$fileName", it)


                listOfMediasToSave.add(
                    RealEstateMedia(
                        uri = it.toString(),
                        name = "photo",
                        realEstateParentId = 1
                    )
                )

            }
        )
    }


    //TODO: move to util class ?
    private fun setupActivityResultForCamera() {
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

    //check datas..
    private fun testForm(): String? {

        var messagetest: String? = ""

        if (viewModelCreate.realEstateVM.typeOfProduct.isNullOrEmpty()) {
            messagetest += "You must to select a type of product \r\n"
        }

        if (viewModelCreate.getMediasListForUI().value.isNullOrEmpty()) {
            messagetest += "You minium one media for the product \r\n"
        }

        if (binding.edittextPrice?.text.toString().isNullOrEmpty()) {
            messagetest += "You must choice a price \r\n"
        }

        if (binding.edittextSurface?.text.toString().isNullOrEmpty()) {
            messagetest += "You must choice a surface \r\n"
        }

        if (binding.edittextNumberBathroom?.text.toString().isNullOrEmpty()) {
            messagetest += "You must choice a number of bathroom \r\n"
        }

        if (binding.edittextNumberBedroom?.text.toString().isNullOrEmpty()) {
            messagetest += "You must choice a number of Bedroom \r\n"
        }

        if (binding.edittextNumberRoom?.text.toString().isNullOrEmpty()) {
            messagetest += "You must choice a number of room \r\n"
        }

        if (binding.edittextDescription?.text.toString().isNullOrEmpty()) {
            messagetest += "You must enter a description \r\n"
        }

        if (binding.edittextStreetNumber?.text.toString().isNullOrEmpty()) {
            messagetest += "You must enter a street number \r\n"
        }

        //steeetname
        //city
        //zip
        //country
        return messagetest

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