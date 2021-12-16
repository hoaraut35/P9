package com.openclassrooms.realestatemanager.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentSearchBinding
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class SearchFragment : Fragment() {

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


    //date
   // private var dateReturn: Long? = null
    private var selectedEntryDate: Long? = null
    private var selectedSoldDate: Long? = null

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


        binding.surfaceRange?.setLabelFormatter {
            "${it}m²"
        }

        binding.searchBtn?.setOnClickListener {

            Log.i("[SQL]", SearchUtils.checkSQLiteVersion())

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

            //Log.i("[SQL]", "$park $station $schoolState")

            Log.i(
                "[DATE]",
                "Date " + LocalDateTime.of(2021, Month.DECEMBER, 15, 14, 27, 30).toString()
            )
            Log.i("[DATE]", "Date util : " + Utils.getTodayDate())

            // getPOIChips()

            //version sqlite 3.18.2

            var queryString = ""
            var args = mutableListOf<Any>()
            var containsCondition = false

            //STEP 1 : SELECT...
            queryString += "SELECT * FROM realEstate_table"


            //STEP2 : INNER JOIN...
            //  queryString += " INNER JOIN RealEstateMedia ON RealEstateMedia.realEstateParentId = realEstate_table.realEstateId GROUP BY RealEstateMedia.realEstateParentId "


            if (maxPrice != null && minPrice != null) {
                containsCondition = true
                queryString += " WHERE price BETWEEN $minPrice AND $maxPrice"
            }

            if (maxSurface != null && minSurface != null) {
                if (containsCondition) {
                    queryString += " AND "
                } else {
                    queryString += " WHERE"
                    containsCondition = true
                }
                queryString += " surface BETWEEN $minSurface AND $maxSurface"
            }

            if (selectedEntryDate != null) {
                if (containsCondition) {
                    queryString += " AND "
                } else {
                    queryString += " WHERE"
                    containsCondition = true
                }
                queryString += " dateOfEntry >= '$selectedEntryDate'"
            }



            if (numberOfPhoto != null) {
                queryString += " INNER JOIN RealEstateMedia ON RealEstateMedia.realEstateParentId = realEstate_table.realEstateId " +
                        "GROUP BY RealEstateMedia.realEstateParentId " +
                        "HAVING COUNT(RealEstateMedia.realEstateParentId) >= $numberOfPhoto"
                //containsOtherCondition = true
            }






            queryString += ";"

            //show query in log...
            Log.i("[SQL]", "My query : $queryString")

            //load query to sqlite...
            searchViewModel.getRealEstateFiltered(
                SimpleSQLiteQuery(
                    queryString,
                    args.toTypedArray()
                )
            ).observe(viewLifecycleOwner) {


                if (it.isNullOrEmpty()) {
                    Snackbar.make(
                        requireView(),
                        "Modify search and try again",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    it?.forEach { myresult ->
                        Log.i("[SQL]", "data" + myresult.realEstateFullData.typeOfProduct)
                        closeFragment()
                    }
                }


            }


        }

        binding.dateBtn?.setOnClickListener {
            if (mDatePicker == null || !mDatePicker!!.isAdded) {
                createDatePicker(it as TextView)
                mDatePicker!!.show(childFragmentManager.beginTransaction(), "DATE_PICKER")
            }
        }

        binding.dateSoldBtn?.setOnClickListener {
            if (mDatePicker == null || !mDatePicker!!.isAdded) {
                createDatePicker(it!! as TextView)
                mDatePicker!!.show(childFragmentManager.beginTransaction(), "DATE_PICKER")
            }
        }

        return binding.root

    }

    private fun closeFragment() {
        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_item_detail) as NavHostFragment

        val navController = navHostFragment.navController
        navController.navigateUp()
    }


    private fun createDatePicker(view: TextView) {

        mDatePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select a date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        mDatePicker?.addOnPositiveButtonClickListener {
            mDate = Utils.epochMilliToLocalDate(it)
            val dateFromFilter : String = mDate!!.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

            if (view.tag == "dateOfCreated"){
                selectedEntryDate = it
            }else
            {
                selectedSoldDate = it
            }



            view.text = dateFromFilter

        }

    }

}