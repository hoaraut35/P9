package com.openclassrooms.realestatemanager.ui.create

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class RealEstateModifier : AdapterRealEstateAdd.InterfacePhotoTitleChanged, Fragment() {

    //binding
    private var _binding: FragmentRealEstateModifierBinding? = null
    private val binding get() = _binding!!

    private var fileNameUri: String? = null

    lateinit var recyclerView: RecyclerView
    private val listOfMediasToSave = mutableListOf<RealEstateMedia>()

    var resultTitle: String? = null

    lateinit var activityResultLauncherForPhoto: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncherForVideo: ActivityResultLauncher<Intent>
    lateinit var activityResultForVideoFromGallery: ActivityResultLauncher<Intent>

    //viewmodels
    private val mainViewModel by viewModels<MainViewModel>()
    private val viewModelCreate by viewModels<ViewModelForCreate>()

    //bundle var
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentRealEstateModifierBinding.inflate(inflater, container, false)
        val rootView = binding.root

        //for setup topbar
        setHasOptionsMenu(true)

        //bind recyclerview
        recyclerView = binding.recyclerview

        //get the type of property
        val valChipGroupType: ChipGroup? = binding.chipGroupType
        valChipGroupType?.setOnCheckedChangeListener { group, checkedId ->
            resultTitle = group.findViewById<Chip>(checkedId)?.text.toString()
            Toast.makeText(requireContext(), "enabled [" + resultTitle + "]", Toast.LENGTH_LONG)
                .show()
        }


        //send type to viewModelCreate
        binding.chipGroupType.setOnCheckedChangeListener { group, checkedId ->
            viewModelCreate.propertyTypeChanged(group.findViewById<Chip>(checkedId)?.text.toString())
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


        //launcher for open video from gallery
        val getImageFromGallery = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {

                //to show image
                binding.imageOfGallery?.setImageURI(it)

                if (it != null) {

                    val bitmap: Bitmap? = MediaStore.Images.Media.getBitmap(
                        context?.applicationContext?.contentResolver, it
                    )

                    val fileName2: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

                    if (bitmap != null) {
                        savePhotoToInternalMemory("Photo_$fileName2", bitmap)
                        recyclerView.adapter?.notifyDataSetChanged()
                    }

                }
            }
        )

        //click listener for take photo from camera
        binding.addPhotoCamera?.setOnClickListener {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activityResultLauncherForPhoto.launch(intent)
        }

        //setup callback for camera
        setupActivityResultForCamera()

        //listener for button click gallery pick
        binding.addPhotoFromMemory?.setOnClickListener {
            getImageFromGallery.launch("image/*")
            // setupGetPhotoFromGalery()
            Log.i("[THOMAS]", "test ouverture photo")
        }

        //setup callback for gallery
        setupActivityResultForGallery()

        //get all chips selected
        //getSelectedChips()

        //get sold state of property
        getSoldStateBtn()

        //save data to database
        saveRealEstateInDB()

        setupViewModel()

        binding.edittextPrice?.addTextChangedListener {
            binding.propertyPriceText.helperText = validPriceText()
        }

        binding.edittextPrice?.setOnFocusChangeListener { _, focused ->
            binding.propertyPriceText.helperText = validPriceText()
        }

        return rootView
    }

    private fun validPriceText(): String? {

        //val regexPrice = Regex("\d")


        return when (binding.edittextPrice?.text.toString()) {
            "" -> "can't be null"
            //regexPrice -> "yes"
            else -> {
                return null
            }
        }
    }

    private fun setupViewModel() {
        viewModelCreate.getUIToShow().observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            Log.i("[MEDIA]", "Get data from viewModel : $it")
            setupRecyclerView(recyclerView, it)
        })
    }

    private fun getSoldStateBtn() {

        val isSoldButton: SwitchMaterial? = binding.isSoldSwitch

        isSoldButton?.setOnClickListener(View.OnClickListener {

            if (isSoldButton.isChecked) {
                Toast.makeText(requireContext(), "enabled", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "disable", Toast.LENGTH_LONG).show()
            }
        })


    }

    private fun getSelectedChips() {


        val listTest = listOf<String>()
        val valChipGroupMulti: ChipGroup? = binding.chipGroupMulti

        valChipGroupMulti?.checkedChipIds?.forEach {
            val chip = binding.chipGroupMulti.findViewById<Chip>(it).text.toString()

            viewModelCreate.listOfChip.add(chip)

            //Log.i("[CHIP]", "chip ${chip.isNotEmpty()}")
        }


    }

    private fun saveRealEstateInDB() {


        mainViewModel.getLAstRowId.observe(viewLifecycleOwner) {

            var lastindex: Int = it

            binding.saveBtn?.setOnClickListener {


//                if (resultTitle.isNullOrEmpty()){
//                    Toast.makeText(requireContext(), "Add a type of product please", Toast.LENGTH_LONG)
//                        .show()
//
//                }


                val prix: Int? = binding.edittextPrice?.text.toString().toInt()
                //val valTypeOfProduct: String? = mainbinding.propertyTypeEdittext?.text.toString()

                val valSurface: Int? = binding.edittextSurface?.text.toString().toInt()
                val valRoomNumber: Int? = binding.edittextNumberRoom?.text.toString().toInt()
                //val valBathRoomNumber : Int? = binding.ed
                val valDescription: String? = binding.edittextDescription?.text?.toString()

                val valStreetNumber: Int? = binding.edittextStrretNumber?.text.toString().toInt()
                val valStreetName: String? = binding.edittextStreetName?.text.toString()
                val valCityZipCode: Int? = binding.edittextCityZipcode?.text.toString().toInt()
                val valCity: String? = binding.edittextCityName?.text.toString()
                //val valCountry : String? = binding.ed

                //insert in database


                mainViewModel.insert(
                    RealEstate(
                        typeOfProduct = resultTitle,
                        price = prix,
                        cityOfProduct = valCity,
                        surface = valSurface,
                        numberOfRoom = valRoomNumber,
                        descriptionOfProduct = valDescription,
                        address = RealEstateAddress(
                            street_name = valStreetName,
                            street_number = valStreetNumber,
                            city = valCity,
                            zip_code = valCityZipCode,
                            country = null
                        ),
                        status = false
                    )
                )

                //add photos
                for (item in viewModelCreate.getUIToShow().value!!) {
                    val long = mainViewModel.insertPhoto(
                        RealEstateMedia(
                            uri = item.uri,
                            realEstateParentId = lastindex,
                            name = item.name
                        )
                    )
                }


                val valChipGroupMulti: ChipGroup? = binding.chipGroupMulti
                var school = false
                var park = false
                var gare = false

                valChipGroupMulti?.checkedChipIds?.forEach {

                    val texte = binding.chipGroupMulti.findViewById<Chip>(it).text.toString()
                    val check = binding.chipGroupMulti.findViewById<Chip>(it).isChecked

                    when (texte) {
                        "Ecole" -> school = check
                        "Parc" -> park = check
                        "Gare" -> gare = check
                    }

                }

                viewModelCreate.insertPOI(
                    RealEstatePOI(
                        school = school,
                        park = park,
                        station = gare,
                        realEstateParentId = 1
                    )
                )

            }
        }
    }

    //function to pick from gallery
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

                fileNameUri = context?.filesDir.toString() + "/" + filename + ".jpg"


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

    //to setup recyclerview
    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        myRealEstateList: List<RealEstateMedia>
    ) {
        val myLayoutManager = LinearLayoutManager(activity)
        myLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = myLayoutManager
        recyclerView.adapter = AdapterRealEstateAdd(myRealEstateList, this)
    }

    //setup menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.realEstateUpdateBtnNew).isVisible = false
        menu.findItem(R.id.realEstateUpdateBtnNew).isVisible = false
    }

    //constructor fragment
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RealEstateModifier.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RealEstateModifier().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        const val PICK_IMAGE = 1
        const val REQUEST_IMAGE_CAPTURE = 2
    }

    override fun onChangedTitlePhoto(title: String, uri: String) {
        viewModelCreate.updateMediaTitle(title, uri)
    }

    override fun onDeletePhoto(media: RealEstateMedia) {
        viewModelCreate.deleteMedia(media)
        context?.deleteFile(media.uri?.substringAfterLast("/"))

    }


}