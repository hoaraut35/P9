package com.openclassrooms.realestatemanager.ui.detail

import android.app.Activity.MODE_PRIVATE
import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.ui.MainViewModel
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstateDetailBinding
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstatePhoto
import com.openclassrooms.realestatemanager.ui.RealEstatePhotosAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    //binding
    private var _binding: FragmentRealEstateDetailBinding? = null
    private val binding get() = _binding!!


    //viewmodel
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {

            if (it.containsKey(ARG_REAL_ESTATE_ID)) {
                item_id_bundle = it.getString(ARG_REAL_ESTATE_ID)
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

        //for test
        mainViewModel.allRealEstate.observe(viewLifecycleOwner) { listRealEstate ->

            val realEstate: RealEstate? = listRealEstate.find { it.id == item_id_bundle?.toInt() }

            if (realEstate != null) {
                binding.textDescriptionDetail.setText(realEstate.descriptionOfProduct)
                binding.qtyNumberRoom.setText(realEstate.numberOfRoom.toString())
                binding.qtyNumberBedroom.setText(realEstate.numberOfBedRoom.toString())
                binding.qtyNumberBathroom.setText(realEstate.numberOfBathRoom.toString())
            }


            //listRealEstate create a list for test
            recyclerViewPhotos?.let { setupRecyclerView(it, listRealEstate) }
        }


        val takePhotoListener = View.OnClickListener { view ->


            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try{

                startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE)

            }catch (e: ActivityNotFoundException){
                //
            }


        }

        binding.textNumberBathroom.setOnClickListener(takePhotoListener)







        return rootView
    }


    //take a photo of property
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("[THOMAS]","Take a photo : " + data.toString())

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            val imageBitmap = data?.extras!!.get("data") as Bitmap
            binding.staticMapView!!.setImageBitmap(imageBitmap)



            val fileName:String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir = File(context?.filesDir, "test")


            File.createTempFile("JPEG_THOMAS_",".jpg",context?.filesDir).apply { Log.i("[THOMAS]", "Photo path :$absolutePath"  ) }

            savePhotoToInternalMemory("test.jpeg",imageBitmap)
        }
    }



   /* private fun loadPhotoFromInternalMEmory(): List<RealEstatePhoto>{
        return withContext(Dispatchers.IO){
            val files = context.filesDir.listFiles()
            files.filter { it.canRead() && it.isFile && it.name.endsWith(".jpg") }.map {
                val bytes = it.readBytes()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

            }
        }
    }

    */

    private fun savePhotoToInternalMemory(filename: String, bmp:Bitmap):Boolean{
        return try{
            context?.openFileOutput("$filename.jpg", MODE_PRIVATE).use { stream ->

                if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95,stream)){
                    throw IOException("erreur compression")
                }
            }
            true

        }catch (e:IOException){
            e.printStackTrace()
            false

        }
    }


  /*  private fun createImageFile( context : Context): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File(context.filesDir)


        return File.createTempFile(
            "",
            "",
            storageDir
        ).apply{

        }

    }

   */


    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        myRealEstateList: List<RealEstate>
    ) {


        var myRealEstateList = listOf<String>("Photo1","Photo2","Photo3","Photo4","Photo5","Photo6")

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