package com.openclassrooms.realestatemanager.ui.search

import android.database.DatabaseUtils
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.openclassrooms.realestatemanager.databinding.FragmentSearchBinding
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.Month
import java.util.*

import com.google.android.material.datepicker.MaterialDatePicker
import com.openclassrooms.realestatemanager.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter


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

    private var minPrice: Int? = null
    private var maxPrice: Int? = null

    private var minSurface: Int? = null
    private var maxSurface: Int? = null

    private var numberOfPhoto: Int? = null

    private var mDatePicker: MaterialDatePicker<Long>? = null
    private var mDate: LocalDate? = null
    private var dateFromFilter: String? = null
    private var dateFromFilterLong: Long? = null


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
                minPrice = slider.values[0].toInt()
                maxPrice = slider.values[1].toInt()
                Log.i("SLIDER", slider.values[0].toString())
                Log.i("SLIDER", slider.values[1].toString())
            }

        }

        binding.priceRange?.addOnSliderTouchListener(touchListener)


        val touchListenerSurface: RangeSlider.OnSliderTouchListener = object :
            RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {

            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                minSurface = slider.values[0].toInt()
                maxSurface = slider.values[1].toInt()
                Log.i("SLIDER", slider.values[0].toString())
                Log.i("SLIDER", slider.values[1].toString())
            }

        }

        binding.surfaceRange?.addOnSliderTouchListener(touchListenerSurface)


        val touchListenerNumberMedia: Slider.OnSliderTouchListener = object :
            Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
            }

            override fun onStopTrackingTouch(slider: Slider) {
                numberOfPhoto = slider.value.toInt()
            }
        }

        binding.mediaNumber?.addOnSliderTouchListener(touchListenerNumberMedia)



//        val version = android.database.sqlite.SQLiteDatabase.create(null).use {
//            DatabaseUtils.stringForQuery(it, "SELECT sqlite_version()", null)
//        }
//
//        Log.i("[SQL]",""+  Build.VERSION.SDK_INT + " " +  version)




        binding.surfaceRange?.setLabelFormatter {
            "${it}m²"
        }

        binding.searchBtn?.setOnClickListener {

            Toast.makeText(requireContext(), "Search", Toast.LENGTH_LONG).show()


            var schoolState = false
            var station = false
            var park = false

            var IntschoolState = 0
            var Intstation = 0
            var Intpark = 0


            val pointsOfInterestChipGroup: ChipGroup = binding.chipGroupPoi!!

            pointsOfInterestChipGroup.checkedChipIds.forEach { chipItem ->
                val chipText =
                    pointsOfInterestChipGroup.findViewById<Chip>(chipItem).text.toString()
                val chipState =
                    pointsOfInterestChipGroup.findViewById<Chip>(chipItem).isChecked
                when (chipText) {
                    "Ecole" -> schoolState = chipState
                    "Gare" -> station = chipState
                    "Parc" -> park = chipState
                }

                if (schoolState) {
                    IntschoolState = 1
                } else {
                    IntschoolState = 0
                }


            }
            Log.i("[SQL]", "$park $station $schoolState")
            Log.i("[DATE]", "Date " + LocalDateTime.of(2021, Month.DECEMBER, 15, 14, 27, 30).toString())
            Log.i("[DATE]", "Date util : " + Utils.getTodayDate())

            // getPOIChips()

            //version sqlite 3.18.2

            var queryString = ""
            var args = mutableListOf<Any>()
            var containsCondition = false

            queryString += "SELECT * FROM realEstate_table"
            //queryString += "SELECT * FROM realEstate_table INNER JOIN RealEstatePOI ON RealEstatePOI.realEstateParentId = realEstate_table.realEstateId INNER JOIN RealEstateMedia ON RealEstateMedia.realEstateParentId = realEstate_table.realEstateId GROUP BY RealEstateMedia.realEstateParentId HAVING COUNT(RealEstateMedia.realEstateParentId) >= 1 AND  HAVING  RealEStatePOI.school = $IntschoolState ;"

            //add price
            if (minPrice != null && maxPrice != null) {
                if (containsCondition) {
                    queryString += " AND"
                } else {
                    queryString += " WHERE"
                    containsCondition = true
                }
                queryString += " price BETWEEN $minPrice AND $maxPrice"
            }

            //add surface
            if (minSurface != null && maxSurface != null) {
                if (containsCondition) {
                    queryString += " AND "
                } else {
                    queryString += " WHERE"
                    containsCondition = true
                }
                queryString += " surface BETWEEN $minSurface AND $maxSurface"
            }


            if (dateFromFilter != null){
                if (containsCondition) {
                    queryString += " AND "
                } else {
                    queryString += " WHERE"
                    containsCondition = true
                }

               // var datefromdb : Long =
                //set date to long in database

                //queryString += " dateOfEntry >= '$dateFromFilter' "
                queryString += " dateOfEntry >= '$dateFromFilterLong' "


            }




            //add photo number
            if (numberOfPhoto != null) {
                queryString += " INNER JOIN RealEstateMedia ON realEstateParentId = realEstate_table.realEstateId GROUP BY RealEstateMedia.realEstateParentId HAVING COUNT(RealEstateMedia.realEstateParentId) >= $numberOfPhoto"
            }

        //    queryString += " INNER JOIN RealEstatePOI ON realEstateParentId = realEstate_table.realEstateId GROUP BY RealEstatePOI.realEstateParentId HAVING RealEStatePOI.school >= $IntschoolState AND  RealEStatePOI.park >= $Intpark AND RealEstatePOI.station >= $Intstation"

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





            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_item_detail) as NavHostFragment

            val navController = navHostFragment.navController
            navController.navigateUp()

        }

        binding.dateBtn?.setOnClickListener {
            Log.i("[BTN]","Clic sur date")

            if (mDatePicker == null || !mDatePicker!!.isAdded) {
                createDatePicker()
                mDatePicker!!.show(childFragmentManager.beginTransaction(), "DATE_PICKER")
            }
        }

        return binding.root

    }

    private fun createDatePicker() {
        mDatePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select a date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        mDatePicker?.addOnPositiveButtonClickListener {
            mDate = Utils.epochMilliToLocalDate(it)
            dateFromFilter = mDate!!.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            dateFromFilterLong = it
            binding.dateBtn?.text = dateFromFilter
        }

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