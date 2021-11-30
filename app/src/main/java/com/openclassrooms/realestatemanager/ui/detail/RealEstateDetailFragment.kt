package com.openclassrooms.realestatemanager.ui.detail

import android.app.Activity.MODE_PRIVATE
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstateDetailBinding
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import com.openclassrooms.realestatemanager.models.RealEstateWithMedia
import com.openclassrooms.realestatemanager.ui.MainViewModel
import com.openclassrooms.realestatemanager.ui.updatenew.ViewModelUpdate
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_REAL_ESTATE_ID = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RealEstateDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


@AndroidEntryPoint  //Hilt annotation for fragment
class RealEstateDetailFragment : Fragment() {

    private var item_id_bundle: String? = null
    private var param2: String? = null

    val REQUEST_IMAGE_CAPTURE = 1

    //bind recyclerview
   // val recyclerView: RecyclerView = binding.recyclerview

    //binding
    private var _binding: FragmentRealEstateDetailBinding? = null
    private val binding get() = _binding!!


    //viewmodel
    private val mainViewModel by viewModels<MainViewModel>()
    private val updateViewModel by viewModels<ViewModelUpdate>()

    private val detailViewModel by viewModels<ViewModelDetail>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {

            if (it.containsKey(ARG_REAL_ESTATE_ID)) {
                item_id_bundle = it.getString(ARG_REAL_ESTATE_ID)

                //updateViewModel.loadRealEstateId(item_id_bundle.toString())
                item_id_bundle?.let { it1 -> detailViewModel.setPropertyId(it1?.toInt()) }

            }

            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentRealEstateDetailBinding.inflate(inflater, container, false)

        val rootView = binding.root

        val recyclerViewPhotos: RecyclerView? = binding.recyclerviewPhotos

        setHasOptionsMenu(true);

        mainViewModel.allRealEstateWithPhotos.observe(viewLifecycleOwner) { it ->

            val realEstate: RealEstateWithMedia? = it.find { it.realEstate.realEstateId.toString() == item_id_bundle }

            if (realEstate != null) {


                binding.textDescriptionDetail.setText(realEstate.realEstate.descriptionOfProduct)
                binding.qtyNumberRoom.setText(realEstate.realEstate.numberOfRoom.toString())
                binding.qtyNumberBedroom.setText(realEstate.realEstate.numberOfBedRoom.toString())
                binding.qtyNumberBathroom.setText(realEstate.realEstate.numberOfBathRoom.toString())


                binding.textNumberStreet?.setText(realEstate.realEstate.address?.street_number.toString())
                binding.textStreetName?.setText(realEstate.realEstate.address?.street_name.toString())
                binding.textCityName?.setText(realEstate.realEstate.address?.city.toString())
                binding.textZipcode?.setText(realEstate.realEstate.address?.zip_code.toString())
                binding.textCountry?.setText(realEstate.realEstate.address?.country.toString())

                //binding.textListphotos?.setText(realEstate.photos.toString())
                //setupRecyclerView(recyclerView, it[0].photosList)

               recyclerViewPhotos?.let { setupRecyclerView(it, realEstate.photosList) }
            }
        }



        return rootView
    }



    //take a photo of property
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("[THOMAS]", "Take a photo : " + data.toString())

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras!!.get("data") as Bitmap
            binding.staticMapView!!.setImageBitmap(imageBitmap)


            val fileName: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir = File(context?.filesDir, "test")

            //   val photoUri : Uri = FileProvider.getUriForFile(this, "com.openclassrooms.realestatemanager",storageDir)

            //File.createTempFile("JPEG_THOMAS_",".jpg",context?.filesDir).apply { Log.i("[THOMAS]", "Photo path :$absolutePath"  ) }

            savePhotoToInternalMemory("Photo_$fileName", imageBitmap)


        }
    }




    private fun savePhotoToInternalMemory(filename: String, bmp: Bitmap): Boolean {
        return try {
            context?.openFileOutput("$filename.jpg", MODE_PRIVATE).use { stream ->

                //compress photo
                if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                    throw IOException("erreur compression")
                }


                //  FileProvider.getUriForFile(requireContext(),"com.openclassrooms.realestatemanager.fileprovider",$filename)
                Log.i("[THOMAS]", "chemin " + context?.filesDir)

            }
            true

        } catch (e: IOException) {
            e.printStackTrace()
            false

        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)


        if (item_id_bundle != null || item_id_bundle == ""){
            menu.findItem(R.id.realEstateUpdateBtnNew).isVisible = true
        }else
        {
            menu.findItem(R.id.realEstateUpdateBtnNew).isVisible = false
        }


    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        myRealEstateList: List<RealEstateMedia>
    ) {
        val myLayoutManager = LinearLayoutManager(activity)
        myLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = myLayoutManager
        recyclerView.adapter = RealEstatePhotosAdapter(myRealEstateList)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RealEstateDetailFragment.
         */
        // TODO: Rename and change types and number of parameters


        const val ARG_REAL_ESTATE_ID = "item_id"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RealEstateDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_REAL_ESTATE_ID, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}