package com.openclassrooms.realestatemanager.ui.updatenew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.databinding.FragmentUpdateNewBinding
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class UpdateFragmentNew : UpdateAdapter.InterfacePhotoTitleChanged, Fragment() {

    private val viewModelUpdate by viewModels<ViewModelUpdate>()

    //binding
    private var _binding: FragmentUpdateNewBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //param1 = it.getString(ARG_PARAM1)
            //param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUpdateNewBinding.inflate(inflater, container, false)
        val rootView = binding.root




//        //realestates work
//        detailViewModel.getRealEstatesLiveData().observe(viewLifecycleOwner){
//            Log.i("[OBSERVE]","All element just realestate data : "+ it.toString())
//        }

//        //realestates with full data work
//        detailViewModel.getRealEstatesFullData().observe(viewLifecycleOwner){
//            Log.i("[OBSERVE]","All element full data : "+it.toString())
//        }

        //val recyclerView: RecyclerView? = binding.recyclerview






//        binding.addPhotoCamera?.setText("test")
//        //    binding.edittextDescription?.setText(myRealEstate.descriptionOfProduct)
//        binding.edittextPrice?.setText(myRealEstate.price.toString())
//        binding.edittextCityZipcode?.setText(myRealEstate.address?.zip_code.toString())
//        binding.edittextCityName?.setText(myRealEstate.address?.city.toString())
//        binding.edittextStreetName?.setText(myRealEstate.address?.street_name.toString())





    return rootView

}

private fun setupRecyclerView(recyclerView: RecyclerView, mediaList: List<RealEstateMedia>) {
    val myLayoutManager =
        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    recyclerView.layoutManager = myLayoutManager
    recyclerView.adapter = UpdateAdapter(mediaList, this)
}

companion object {

    @JvmStatic
    fun newInstance(param1: String, param2: String) =
        UpdateFragmentNew().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
}

override fun onChangedTitlePhoto(title: String, uri: String) {

}

override fun onDeletePhoto(media: RealEstateMedia) {

}
}