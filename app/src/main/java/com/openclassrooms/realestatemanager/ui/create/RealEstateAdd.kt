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
import com.openclassrooms.realestatemanager.models.RealEstatePOI
import com.openclassrooms.realestatemanager.models.RealEstatePhoto
import com.openclassrooms.realestatemanager.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class RealEstateModifier : AdapterRealEstateAdd.InterfacePhotoTitleChanged, Fragment()  {

    //binding
    private var _binding: FragmentRealEstateModifierBinding? = null
    private val binding get() = _binding!!
    private var fileNameUri: String? = null

    // private var  recyclerview : RecyclerView = binding.recyclerview
    private val listOfPhotosToSave = mutableListOf<RealEstatePhoto>()

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    //viewmodel
    private val mainViewModel by viewModels<MainViewModel>()

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
        val recyclerView = binding.recyclerview

        //setupActionAfterGetImageFromGallery()

        //setup action after click on gallery
        val getImageFromGallery = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                binding.imageOfGallery?.setImageURI(it)

                val bitmap: Bitmap? = MediaStore.Images.Media.getBitmap(
                    context?.applicationContext?.contentResolver, it
                )

                val fileName2: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())


                if (bitmap != null) {
                    savePhotoToInternalMemory("Photo_$fileName2", bitmap)
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            }
        )

        //listener for camera pick
        binding.addPhotoCamera?.setOnClickListener {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activityResultLauncher.launch(intent)
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

        //get property type
        getPropertyType()

        //get all chips selected
        getSelectedChips()

        //get sold state of property
        getSoldStateBtn()

        //save data to database
        saveRealEstateInDB()

        //setup recyclerview
        setupRecyclerView(recyclerView, listOfPhotosToSave)



        recyclerView.adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onChanged() {
                super.onChanged()
                Toast.makeText(requireContext(), "Evenement on recyclerview" , Toast.LENGTH_LONG).show()


          //      Log.i("[UPDATE]","recyclerview" + recyclerView.getChildViewHolder(binding.recyclerview.focusedChild).itemView.findViewById<EditText>(R.id.photo_title))

            }
        })


        //valChipGroupMulti?.checkedChipIds?.forEach {
          //  val chip = binding.chipGroupMulti.findViewById<Chip>(it).text.toString()
            //Log.i("[CHIP]", "chip $chip.")
        //}


        return rootView
    }

    private fun setupActionAfterGetImageFromGallery() {
        TODO("Not yet implemented")
    }

    private fun getPropertyType() {
        //single mode
        val valChipGroup: ChipGroup? = binding.chipGroup
        valChipGroup?.setOnCheckedChangeListener { group, checkedId ->
            val title = group.findViewById<Chip>(checkedId)?.text
            Toast.makeText(requireContext(), "enabled" + title, Toast.LENGTH_LONG).show()
        }

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

        val valChipGroupMulti: ChipGroup? = binding.chipGroupMulti

        valChipGroupMulti?.checkedChipIds?.forEach {
            val chip = binding.chipGroupMulti.findViewById<Chip>(it).text.toString()
            Log.i("[CHIP]", "chip $chip.")
        }

    }

    private fun saveRealEstateInDB() {

        binding.saveBtn?.setOnClickListener {


            val prix: Int = binding.edittextPrice?.text.toString().toInt()
            val valTypeOfProduct: String? = binding.propertyTypeEdittext?.text.toString()

            val valSurface: Int? = binding.edittextSurface?.text.toString().toInt()
            val valRoomNumber: Int? = binding.edittextNumberRoom?.text.toString().toInt()
            //val valBathRoomNumber : Int? = binding.ed
            val valDescription: String? = binding.edittextDescription?.text.toString()

            val valStreetNumber: Int? = binding.edittextStrretNumber?.text.toString().toInt()
            val valStreetName: String? = binding.edittextStreetName?.text.toString()
            val valCityZipCode: Int? = binding.edittextCityZipcode?.text.toString().toInt()
            val valCity: String? = binding.edittextCityName?.text.toString()
            //val valCountry : String? = binding.ed

            //insert in database
            mainViewModel.insert(
                RealEstate(
                    typeOfProduct = valTypeOfProduct,
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
                    poi = RealEstatePOI(poitype = "Ecole"),
                    status = false,
                    photos = RealEstatePhoto(
                        realEstateParentId = 1,
                        name = "photi",
                        uri = "test uri"
                    )
                )
            )




            for (item in listOfPhotosToSave) {

                mainViewModel.insertPhoto(
                    RealEstatePhoto(
                        uri = item.uri,
                        realEstateParentId = 1,
                        name = item.name
                    )
                )

            }


            // mainViewModel.insert


            //mainViewModel.insertPhoto(
//                RealEstatePhoto(
//                    name = "test",
//                    realEstateParentId = 1,
//                    uri = fileNameUri
//                )
//            )
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


                //listOfPhotosToSave.add(it.toString())


            }
        )
    }


    //TODO: move to util class ?
    private fun setupActivityResultForCamera() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
                if (result?.resultCode == Activity.RESULT_OK) {
                    var bitmap = result!!.data!!.extras!!.get("data") as Bitmap
                    binding.imageOfGallery.setImageBitmap(bitmap)

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

                Log.i("[THOMAS]", "chemin " + context?.filesDir + "/" + filename + ".jpg")

                Log.i("[THOMAS]", "chemin " + context?.filesDir)


                fileNameUri = context?.filesDir.toString() + "/" + filename + ".jpg"

                binding.textUri.setText(context?.filesDir.toString() + "/" + filename + ".jpg")


                //ajout
                listOfPhotosToSave.add(RealEstatePhoto(uri = fileNameUri!!, name = "test", realEstateParentId = 1))


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
        myRealEstateList: List<RealEstatePhoto>
    ) {
        val myLayoutManager = LinearLayoutManager(activity)
        myLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = myLayoutManager
        recyclerView.adapter = AdapterRealEstateAdd(myRealEstateList, this  , requireContext())
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.findItem(R.id.realEstateUpdateBtn).isVisible = false

        menu.findItem(R.id.realEstateCreateBtn).isVisible = false

    }


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

    override fun onChangedTitlePhoto(title: String, uri: String?) {
        Log.i("[UPDATE]", "Liste to save : $listOfPhotosToSave")
        listOfPhotosToSave.find { it.uri == uri }?.name = title
    }
}