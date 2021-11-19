package com.openclassrooms.realestatemanager.ui.addupdate

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
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstateModifierBinding
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RealEstateModifier.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class RealEstateModifier : Fragment() {

    //binding
    private var _binding: FragmentRealEstateModifierBinding? = null
    private val binding get() = _binding!!


    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    //viewmodel
    private val mainViewModel by viewModels<MainViewModel>()

    // TODO: Rename and change types of parameters
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRealEstateModifierBinding.inflate(inflater, container, false)

        val rootView = binding.root

        //bind recyclerview
        val recyclerView: RecyclerView = binding.recyclerview

        //to get image from gallery
        val getImageFromGallery = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {binding.imageOfGallery?.setImageURI(it)}
        )

        //to get image from camera
        val getPhotoFromCamera = registerForActivityResult(
            ActivityResultContracts.TakePicture(),
            ActivityResultCallback {it}
        )

        //listener for camera
        binding.addPhotoCamera?.setOnClickListener {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activityResultLauncher.launch(intent)
        }

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

        //from gallery
        binding.addPhotoFromMemory?.setOnClickListener {
            getImageFromGallery.launch("image/*")
            // setupGetPhotoFromGalery()
            Log.i("[THOMAS]", "test ouverture photo")
        }

        //insert data into database
        binding.saveBtn?.setOnClickListener {
            mainViewModel.insert(RealEstate(price = 1000))
        }

        //for test
        mainViewModel.allRealEstate.observe(viewLifecycleOwner) { listRealEstate ->
            //  var myRealEstateList = listOf<String>("Photo1","Photo2","Photo3","Photo4","Photo5","Photo6")

            setupRecyclerView(recyclerView, listRealEstate)
        }

        return rootView
    }


    //TODO: store outside function
    private fun savePhotoToInternalMemory(filename: String, bmp: Bitmap): Boolean {
        return try {
            context?.openFileOutput("$filename.jpg", Activity.MODE_PRIVATE).use { stream ->

                //compress photo
                if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                    throw IOException("erreur compression")
                }

              //  Log.i("[THOMAS]", "chemin " + context?.filesDir)

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
        myRealEstateList: List<RealEstate>,
    ) {


        val myLayoutManager = LinearLayoutManager(activity)
        myLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = myLayoutManager
        recyclerView.adapter = MyNewAddPhotoVideoAdapter(myRealEstateList)
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
}