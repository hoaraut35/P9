package com.openclassrooms.realestatemanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstateDetailBinding
import com.openclassrooms.realestatemanager.models.RealEstate
import dagger.hilt.android.AndroidEntryPoint

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

      //  binding.textDescription.setText(item_id_bundle)


        //for test
        mainViewModel.allRealEstate.observe(viewLifecycleOwner) { listRealEstate ->


               val realEstate : RealEstate? = listRealEstate.find { it.id == item_id_bundle?.toInt()   }


            if (realEstate != null) {

                binding.textDescriptionDetail.setText(realEstate.descriptionOfProduct)
                binding.qtyNumberRoom.setText(realEstate.numberOfRoom.toString())
                binding.qtyNumberBedroom.setText(realEstate.numberOfBedRoom.toString())
                binding.qtyNumberBathroom.setText(realEstate.numberOfBathRoom.toString())

            }


        }

        return rootView
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