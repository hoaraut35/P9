package com.openclassrooms.realestatemanager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.openclassrooms.realestatemanager.databinding.FragmentListRealestateBinding
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstateDetailBinding
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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //binding
    private var _binding: FragmentRealEstateDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {

            if (it.containsKey(ARG_REAL_ESTATE_ID)){
                param1 = it.getString(ARG_REAL_ESTATE_ID)
            }


                //Log.i("[THOMAS]","id real estate trouvé")


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


        //const val  ARG_REAL_ESTATE_ID

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