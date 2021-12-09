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

   // private var itemIdBundle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            if (it.containsKey(RealEstateDetailFragment.ARG_REAL_ESTATE_ID)) {
//                itemIdBundle = it.getString(RealEstateDetailFragment.ARG_REAL_ESTATE_ID)
//                Log.i("[UPDATE]", "bien " + itemIdBundle)
//            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUpdateNewBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val recyclerViewMedias: RecyclerView? = binding.recyclerview
        //val valChipGroupType: ChipGroup? = binding.chipGroupType



//        valChipGroupType?.setOnCheckedChangeListener { group, checkedId ->
//            var resultTitle = group.findViewById<Chip>(checkedId)?.text.toString()
//            Toast.makeText(requireContext(), "enabled [" + resultTitle + "]", Toast.LENGTH_LONG)
//                .show()
//        }


     //   valChipGroupType.childCount

//        valChipGroupType?.forEach {
//            if (it.text == ){
//                Toast.makeText(requireContext(), "enabled [" + it.id , Toast.LENGTH_LONG)
//                    .show()
//            }
//        }

        viewModelUpdate.getRealEstateFullById().observe(viewLifecycleOwner) {

            binding.edittextPrice?.setText(it.realEstateFullData.price?.toString())
            binding.edittextSurface?.setText(it.realEstateFullData.surface.toString())
            binding.edittextDescription?.setText(it.realEstateFullData.descriptionOfProduct)
            binding.edittextStreetNumber?.setText(it.realEstateFullData.address?.street_number.toString())
            binding.edittextStreetName?.setText(it.realEstateFullData.address?.street_name)
            binding.edittextCityZipcode?.setText(it.realEstateFullData.address?.zip_code.toString())
            binding.edittextCityName?.setText(it.realEstateFullData.address?.city)

            it.mediaList?.let { it1 ->
                if (recyclerViewMedias != null) {
                    setupRecyclerView(recyclerViewMedias, it1)
                }
            }

            //get name of chip selected
            var typeOfProduct : String? = it.realEstateFullData.typeOfProduct

        //    valChipGroupType.setS





            when(it.poi?.station){
                true -> binding.stationChip?.isChecked  = true
                false -> binding.stationChip?.isChecked = false
                else -> binding.stationChip?.isChecked = false
            }

            when(it.poi?.school){
                true -> binding.schoolChip?.isChecked  = true
                false -> binding.schoolChip?.isChecked = false
                else -> binding.schoolChip?.isChecked = false
            }

            when(it.poi?.park){
                true -> binding.parcChip?.isChecked  = true
                false -> binding.parcChip?.isChecked = false
                else -> binding.parcChip?.isChecked = false
            }



        }


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