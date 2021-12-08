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
import com.openclassrooms.realestatemanager.ui.detail.RealEstateDetailFragment
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class UpdateFragmentNew : UpdateAdapter.InterfacePhotoTitleChanged, Fragment() {

    private val viewModelUpdate by viewModels<ViewModelUpdate>()

    //binding
    private var _binding: FragmentUpdateNewBinding? = null
    private val binding get() = _binding!!

    private var itemIdBundle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(RealEstateDetailFragment.ARG_REAL_ESTATE_ID)) {
                itemIdBundle = it.getString(RealEstateDetailFragment.ARG_REAL_ESTATE_ID)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUpdateNewBinding.inflate(inflater, container, false)
        val rootView = binding.root

//        viewModelUpdate.getRealEstateFullById(itemIdBundle!!.toInt()).observe(viewLifecycleOwner) {
        viewModelUpdate.getRealEstateFullById(1).observe(viewLifecycleOwner) {

            binding.edittextCityZipcode?.setText(it.realEstateFullData.price!!)
            binding.edittextCityName?.setText(it.realEstateFullData.address!!.city!!)




        }

        //realestates work
        viewModelUpdate
   //     detailViewModel.getRealEstatesLiveData().observe(viewLifecycleOwner){
//            Log.i("[OBSERVE]","All element just realestate data : "+ it.toString())
      //  }

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