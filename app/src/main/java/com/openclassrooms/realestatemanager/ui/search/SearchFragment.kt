package com.openclassrooms.realestatemanager.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.openclassrooms.realestatemanager.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val searchViewModel by viewModels<SearchViewModel>()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentSearchBinding.inflate(inflater, container, false)




        binding.priceRange?.setLabelFormatter {
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("USD")
            format.format(it.toDouble())
        }


        val touchListener: RangeSlider.OnSliderTouchListener = object :
            RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {

            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                Log.i("SLIDER",slider.values[0].toString())
                Log.i("SLIDER",slider.values[1].toString())

                //Toast.makeText(requireContext(), slider.values[0].toString(), Toast.LENGTH_LONG).show()

            }

        }

        binding.priceRange?.addOnSliderTouchListener(touchListener)


          binding.priceRange?.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
          //    Toast.makeText(requireContext(), value.toString(), Toast.LENGTH_LONG).show()
        }


                binding . surfaceRange ?. setLabelFormatter {
            "${it}m²"
        }

                binding . searchBtn ?. setOnClickListener {

            Toast.makeText(requireContext(), "Search", Toast.LENGTH_LONG).show()

            var queryString = ""
            var args = mutableListOf<Any>()
            var containsCondition = false

            queryString += "SELECT * FROM realEstate_table"

            //add price
            queryString += " WHERE"
            queryString += " price BETWEEN 10 AND 1000"
            //add surface
            queryString += " AND "
            queryString += " surface BETWEEN 10 AND 50"
            queryString += ";"

            searchViewModel.getRealEstateFiltered(
                SimpleSQLiteQuery(
                    queryString,
                    args.toTypedArray()
                )
            ).observe(viewLifecycleOwner) {
                it?.forEach {
                    Log.i("[SQL]", "data" + it.realEstateFullData.typeOfProduct)
                }
            }

        }

            return binding.root

    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}