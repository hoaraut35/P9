package com.openclassrooms.realestatemanager.ui.addupdate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.databinding.FragmentRealEstateModifierBinding
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.ui.MainViewModel
import com.openclassrooms.realestatemanager.ui.detail.RealEstatePhotosAdapter
import com.openclassrooms.realestatemanager.ui.list.MyRecyclerViewAdapterBis
import dagger.hilt.android.AndroidEntryPoint

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





        binding.saveBtn?.setOnClickListener {



            //val priceResult : Int = Value.to  binding.inputPrice.text


            mainViewModel.insert(RealEstate(price =1000))

        }


        //for test
        mainViewModel.allRealEstate.observe(viewLifecycleOwner) { listRealEstate ->
          //  var myRealEstateList = listOf<String>("Photo1","Photo2","Photo3","Photo4","Photo5","Photo6")

            setupRecyclerView(recyclerView, listRealEstate)
        }


        return rootView
    }


    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        myRealEstateList: List<RealEstate>,
     //   onClickListener: View.OnClickListener
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
    }
}