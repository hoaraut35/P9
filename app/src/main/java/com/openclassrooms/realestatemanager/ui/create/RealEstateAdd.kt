package com.openclassrooms.realestatemanager.ui.create

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
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
import com.google.android.material.chip.ChipGroup
import com.google.android.material.switchmaterial.SwitchMaterial
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstateModifierBinding
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateAddress
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import com.openclassrooms.realestatemanager.models.RealEstatePOI
import com.openclassrooms.realestatemanager.ui.MainViewModel
import com.openclassrooms.realestatemanager.ui.update.UpdateAdapter
import com.openclassrooms.realestatemanager.utils.CreateUtils
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class RealEstateModifier : CreateAdapter.InterfacePhotoTitleChanged, Fragment() {

    private var _binding: FragmentRealEstateModifierBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by viewModels<MainViewModel>()
    private val viewModelCreate by viewModels<CreateViewModel>()

    private val listOfMediasToSave = mutableListOf<RealEstateMedia>()

    private lateinit var activityResultLauncherForPhoto: ActivityResultLauncher<Intent>
    private lateinit var activityResultLauncherForVideo: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRealEstateModifierBinding.inflate(inflater, container, false)
        val rootView = binding.root

        //for setup topBar
        setHasOptionsMenu(true)

        //bind recyclerview
        val recyclerView = binding.recyclerview

        //get the type of property
        // val valChipGroupType = binding.chipGroupType
        var resultType: String? = null

        binding.chipGroupType.setOnCheckedChangeListener { group, checkedId ->
            viewModelCreate.realEstateVM.typeOfProduct =
                group.findViewById<Chip>(checkedId)?.text.toString()
        }

        binding.chipGroupPoi.setOnCheckedChangeListener { group, checkedId ->
            viewModelCreate.propertyTypeChanged(group.findViewById<Chip>(checkedId)?.text.toString())
        }


        binding.edittextPrice?.addTextChangedListener {
            binding.propertyPriceText.helperText =
                CreateUtils.validPriceText(binding.edittextPrice!!.text)
        }


        //setupActionAfterGetImageFromGallery()

        //click listener to get video from camera
        binding.addVideoFromCamera?.setOnClickListener {
            val intentVideo = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            activityResultLauncherForVideo.launch(intentVideo)
        }

        //get result for a video from the camera
        activityResultLauncherForVideo =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
                if (result!!.resultCode == Activity.RESULT_OK) {
                    viewModelCreate.addMediaToList(
                        RealEstateMedia(
                            uri = result.data?.data.toString(),
                            realEstateParentId = 1,
                            name = "video"
                        )
                    )

                    recyclerView.adapter?.notifyDataSetChanged()
                }
            }

        //get result for a video from gallery
        val getVideoFromGallery = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {

                if (it != null) {
                    viewModelCreate.addMediaToList(
                        RealEstateMedia(
                            uri = it.toString(),
                            realEstateParentId = 1,
                            name = "video"
                        )
                    )

                    recyclerView.adapter?.notifyDataSetChanged()
                }
            }
        )

        //click listener for open video from gallery
        binding.addVideoFromMemory?.setOnClickListener {
            getVideoFromGallery.launch("video/*")
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

                    if (bitmap != null) {
                        val fileNameUri: String?
                        fileNameUri =
                            context?.filesDir.toString() + "/" + fileName + dateFileName + ".jpg"

                        savePhotoToInternalMemory("$dateFileName", bitmap)
                        recyclerView!!.adapter?.notifyDataSetChanged()

                      //CreateUtils.savePhotoToInternalMemory("Photo_$dateFileName", bitmap)
                        //recyclerViewMedias!!.adapter?.notifyDataSetChanged()
                    }

                }
            }
        )




//        //launcher for open video from gallery
//        val getImageFromGallery = registerForActivityResult(
//            ActivityResultContracts.GetContent(),
//            ActivityResultCallback {uri->
//
//                //to show image
//                binding.imageOfGallery?.setImageURI(uri)
//
//                if (uri != null) {
//
//                    val bitmap: Bitmap? = MediaStore.Images.Media.getBitmap(
//                        context?.applicationContext?.contentResolver, uri
//                    )
//
//                    val dateFileName: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//                    val fileName = "Photo_"
//
//                    if (bitmap != null) {
//
//                        var fileNameUri: String?
//                        fileNameUri =
//                            context?.filesDir.toString() + "/" + fileName + dateFileName + ".jpg"
//
//
//                        if (CreateUtils.savePhotoToInternalMemory(
//                                requireContext(),
//                                "Photo_$dateFileName",
//                                bitmap
//                            )
//                        ) {
//                            //add in viewmodel list
//                            viewModelCreate.addMediaToList(
//                                RealEstateMedia(
//                                    uri = fileNameUri,
//                                    name = "media",
//                                    realEstateParentId = 1
//                                )
//                            )
//
//                        }
//
//
//
//
//
//                        recyclerView.adapter?.notifyDataSetChanged()
//                    }
//
//                }
//            }
//        )

        //click listener for take photo from camera
        binding.addPhotoCamera.setOnClickListener {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activityResultLauncherForPhoto.launch(intent)
        }

        //setup callback for camera
        setupActivityResultForCamera()



        binding.addPhotoFromMemory?.setOnClickListener {
            getImageFromGallery.launch("image/*")
        }




        //setup callback for gallery
        setupActivityResultForGallery()

        //get all chips selected
        //getSelectedChips()

        //get sold state of property
        getSoldStateBtn()

        //save data to database
        saveRealEstateInDB()

        setupViewModel(recyclerView)

        binding.edittextPrice?.addTextChangedListener {
            binding.propertyPriceText.helperText =
                CreateUtils.validPriceText(binding.edittextPrice!!.text)
        }

        binding.edittextPrice?.setOnFocusChangeListener { _, focused ->
            binding.propertyPriceText.helperText =
                CreateUtils.validPriceText(binding.edittextPrice!!.text)
        }

        binding.edittextDescription?.addTextChangedListener {
            binding.propertyDescriptionText.helperText =
                CreateUtils.validPriceText(binding.edittextDescription!!.text)
        }

        binding.edittextDescription?.setOnFocusChangeListener { _, focused ->
            binding.propertyDescriptionText.helperText =
                CreateUtils.validPriceText(binding.edittextDescription!!.text)
        }


        mainViewModel.observeRowId().observe(viewLifecycleOwner) {


            //Toast.makeText(requireContext(),"Event on row id",Toast.LENGTH_LONG).show()
            notification("RealEsatzte", "Sauvegarde temrinée")
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_item_detail) as NavHostFragment

            val navController = navHostFragment.navController
            navController.navigateUp()

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
                        ItemTouchHelper.START or ItemTouchHelper.END,
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
                        recyclerView.adapter!!.notifyItemMoved(fromPosition, toPosition)


                        val adapterList = (binding.recyclerview.adapter as CreateAdapter).mediaList
                        if (!adapterList.isNullOrEmpty()){
                            Log.i("[LIST]", "list adapte r: $adapterList")
                        }


                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        //SWIPE DELETE FEATURE
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


    private fun notification(task: String, desc: String) {
        val manager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    "realEstate",
                    "realEstate",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            manager.createNotificationChannel(channel)
        }
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(requireContext(), "realEstate")
                .setContentTitle("RealEsatate")
                .setStyle(NotificationCompat.BigTextStyle().bigText("Sauvegarde terminée"))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(R.mipmap.ic_launcher)
        manager.notify(1, builder.build())
    }


    private fun getSoldStateBtn() {

        val isSoldButton: SwitchMaterial = binding.isSoldSwitch

        isSoldButton.setOnClickListener(View.OnClickListener {

            if (isSoldButton.isChecked) {
                Toast.makeText(requireContext(), "enabled", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "disable", Toast.LENGTH_LONG).show()
            }
        })


    }

    private fun getSelectedChips() {

        val listTest = listOf<String>()
        val valChipGroupMulti: ChipGroup? = binding.chipGroupPoi

        valChipGroupMulti?.checkedChipIds?.forEach {
            val chip = binding.chipGroupPoi.findViewById<Chip>(it).text.toString()

            viewModelCreate.listOfChip.add(chip)

            Log.i("[CHIP]", "chip ${chip.isNotEmpty()}")
        }

    }

    private fun getSelectedChipsType() {

        val listTest = listOf<String>()
        val valChipGroupMulti: ChipGroup = binding.chipGroupType

        valChipGroupMulti.checkedChipIds.forEach {
            val chip = binding.chipGroupType.findViewById<Chip>(it).text.toString()

            viewModelCreate.listOfChip.add(chip)

            Log.i("[CHIP]", "chip ${chip.isNotEmpty()}")
        }

    }


    private fun saveRealEstateInDB() {

        mainViewModel.getLAstRowId.observe(viewLifecycleOwner) {

            val lastIndex: Int = it

            binding.saveBtn.setOnClickListener {


                getSelectedChips()


                mainViewModel.insertRealEstate(
                    RealEstate(

                        typeOfProduct = viewModelCreate.realEstateVM.typeOfProduct,
                        price = binding.edittextPrice?.text.toString().toInt(),
                        surface = binding.edittextSurface?.text.toString().toInt(),
                        numberOfRoom = binding.edittextNumberRoom?.text.toString().toInt(),
                        numberOfBathRoom = binding.edittextNumberBathroom?.text.toString().toInt(),
                        numberOfBedRoom = binding.edittextNumberBedroom?.text.toString().toInt(),
                        descriptionOfProduct = binding.edittextDescription?.text?.toString(),
                        address = RealEstateAddress(
                            street_name = binding.edittextStreetName?.text.toString(),
                            street_number = binding.edittextStreetNumber?.text.toString().toInt(),
                            city = binding.edittextCityName?.text.toString(),
                            zip_code = binding.edittextCityZipcode?.text.toString().toInt(),
                            country = null
                        ),
                        status = false
                    )
                )


                //add medias to database
                if (!viewModelCreate.getMediasListForUI().value.isNullOrEmpty()) {
                    for (item in viewModelCreate.getMediasListForUI().value!!) {
                        val long = mainViewModel.insertPhoto(
                            RealEstateMedia(
                                uri = item.uri,
                                realEstateParentId = lastIndex,
                                name = item.name
                            )
                        )
                    }

                }


                val valChipGroupMulti: ChipGroup = binding.chipGroupPoi
                var school = false
                var park = false
                var gare = false

                valChipGroupMulti.checkedChipIds.forEach {

                    val chipText = binding.chipGroupPoi.findViewById<Chip>(it).text.toString()
                    val check = binding.chipGroupPoi.findViewById<Chip>(it).isChecked

                    when (chipText) {
                        "Ecole" -> school = check
                        "Parc" -> park = check
                        "Gare" -> gare = check
                    }

                }

                viewModelCreate.insertPOI(
                    RealEstatePOI(
                        poiId = 1,
                        school = school,
                        park = park,
                        station = gare,
                        realEstateParentId = 1
                    )
                )

            }
        }
    }

    //TODO: move to utils
    private fun setupActivityResultForGallery() {
        //to get image from gallery
        val getImageFromGallery = registerForActivityResult(
            ActivityResultContracts.GetContent(),

            ActivityResultCallback {
                binding.imageOfGallery?.setImageURI(it)
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
        activityResultLauncherForPhoto =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->

                if (result?.resultCode == Activity.RESULT_OK) {
                    val bitmap = result.data!!.extras!!.get("data") as Bitmap
                    val fileName: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                    //val storageDir = File(context?.filesDir, "test")
                    CreateUtils.savePhotoToInternalMemory(
                        requireContext(),
                        "Photo_$fileName",
                        bitmap
                    )
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

    //setup menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.realEstateUpdateBtnNew).isVisible = false
        menu.findItem(R.id.realEstateUpdateBtnNew).isVisible = false
    }

    //callBack recyclerView
    override fun onChangedTitlePhoto(title: String, uri: String) {
        viewModelCreate.updateMediaTitle(title, uri)
    }

    //callBack recyclerView
    override fun onDeletePhoto(media: RealEstateMedia) {
        viewModelCreate.deleteMedia(media)
        context?.deleteFile(media.uri?.substringAfterLast("/"))
    }

}