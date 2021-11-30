package com.openclassrooms.realestatemanager.ui.update

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstateUpdateBinding
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class RealEstateUpdate : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    //binding
    private var _binding: FragmentRealEstateUpdateBinding? = null
    private val binding get() = _binding!!


    private val viewModelUpdate by viewModels<ViewModelUpdate>()


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

        // Inflate the layout for this fragment
        _binding = FragmentRealEstateUpdateBinding.inflate(inflater, container, false)
        val rootView = binding.root





        viewModelUpdate.getEstate()?.observe(viewLifecycleOwner) { it ->
            Log.i("[VM]", "observer " + it.typeOfProduct)
        }


        Log.i("[GET]","get from two vizewmodxel " + viewModelUpdate.getEstate())

//        viewModelUpdate.getRealEstate().observe(viewLifecycleOwner) { estate ->
//            Log.i("[VM]", "update frag  : $estate")
//        }

        return inflater.inflate(R.layout.fragment_real_estate_update, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setId?.setOnClickListener{
            Log.i("[VM]", "Click")
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RealEstateUpdate.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RealEstateUpdate().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}