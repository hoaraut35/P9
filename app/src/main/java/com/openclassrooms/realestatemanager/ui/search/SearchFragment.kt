package com.openclassrooms.realestatemanager.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val searchViewModel by viewModels<SearchViewModel>()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        //price label
        binding.priceRange?.setLabelFormatter {
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("USD")
            format.format(it.toDouble())
        }

        //price listener
        val priceListener: RangeSlider.OnSliderTouchListener = object :
            RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                searchViewModel.minPrice = slider.values[0].toInt()
                searchViewModel.maxPrice = slider.values[1].toInt()
            }
        }
        binding.priceRange?.addOnSliderTouchListener(priceListener)

        //surface label
        binding.surfaceRange?.setLabelFormatter {
            "${it}m²"
        }

        //surface listener
        val surfaceListener: RangeSlider.OnSliderTouchListener = object :
            RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                searchViewModel.minSurface = slider.values[0].toInt()
                searchViewModel.maxSurface = slider.values[1].toInt()
            }
        }
        binding.surfaceRange?.addOnSliderTouchListener(surfaceListener)

        //media number listener
        val mediaNumberListener: Slider.OnSliderTouchListener = object :
            Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
            }

            override fun onStopTrackingTouch(slider: Slider) {
                searchViewModel.numberOfPhoto = slider.value.toInt()
            }
        }
        binding.mediaNumber?.addOnSliderTouchListener(mediaNumberListener)

        //search button listener
        binding.searchBtn?.setOnClickListener {

            Log.i("[SQL]", SearchUtils.checkSQLiteVersion())

            Toast.makeText(requireContext(), "Search in progress...", Toast.LENGTH_SHORT).show()

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


            if (searchViewModel.maxPrice != null && searchViewModel.minPrice != null) {
                containsCondition = true
                queryString += " WHERE price BETWEEN ${searchViewModel.minPrice} AND ${searchViewModel.maxPrice}"
            }

            if (searchViewModel.maxSurface != null && searchViewModel.minSurface != null) {
                if (containsCondition) {
                    queryString += " AND "
                } else {
                    queryString += " WHERE"
                    containsCondition = true
                }
                queryString += " surface BETWEEN ${searchViewModel.minSurface} AND ${searchViewModel.maxSurface}"
            }

            if (searchViewModel.selectedEntryDate != null) {
                if (containsCondition) {
                    queryString += " AND "
                } else {
                    queryString += " WHERE"
                    containsCondition = true
                }
                queryString += " dateOfEntry >= '${searchViewModel.selectedEntryDate}'"
            }

            if (searchViewModel.selectedSoldDate != null) {
                if (containsCondition) {
                    queryString += " AND "
                } else {
                    queryString += " WHERE"
                    containsCondition = true
                }
                queryString += " releaseDate >= '${searchViewModel.selectedSoldDate}'"
            }

            if (searchViewModel.numberOfPhoto != null) {
                queryString += " INNER JOIN RealEstateMedia ON RealEstateMedia.realEstateParentId = realEstate_table.realEstateId " +
                        "GROUP BY RealEstateMedia.realEstateParentId " +
                        "HAVING COUNT(RealEstateMedia.realEstateParentId) >= ${searchViewModel.numberOfPhoto}"
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

                    //update shared repository
                    searchViewModel.setResultListFromSearch(it.toMutableList())

                    it?.forEach { mySearchResult ->
                        Log.i("[SQL]", "data" + mySearchResult.realEstateFullData.typeOfProduct)



                        closeFragment()
                    }
                }


            }


        }

        binding.dateBtn?.setOnClickListener {
            createDatePicker(it as TextView, childFragmentManager)
        }

        binding.dateSoldBtn?.setOnClickListener {
            createDatePicker(it!! as TextView, childFragmentManager)
        }

        return binding.root

    }

    private fun closeFragment() {
        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_item_detail) as NavHostFragment

        val navController = navHostFragment.navController
        navController.navigateUp()
    }

    private fun createDatePicker(view: TextView, childFragmentManager: FragmentManager) {

        var mDatePicker: MaterialDatePicker<Long>? = null

        if (mDatePicker == null || !mDatePicker!!.isAdded) {
            mDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select a date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            mDatePicker?.addOnPositiveButtonClickListener {
                searchViewModel.mDate = Utils.epochMilliToLocalDate(it)
                val dateFromFilter: String =
                    searchViewModel.mDate!!.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

                if (view.tag == "dateOfCreated") {
                    searchViewModel.selectedEntryDate = it
                } else {
                    searchViewModel.selectedSoldDate = it
                }

                view.text = dateFromFilter

            }
            mDatePicker.show(childFragmentManager.beginTransaction(), "DATE_PICKER")
        }
    }

    override fun onResume() {
        super.onResume()

        if (searchViewModel.selectedEntryDate != null) {
            binding.dateBtn?.text = Utils.epochMilliToLocalDate(searchViewModel.selectedEntryDate)
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        }

        if (searchViewModel.selectedSoldDate != null) {
            binding.dateSoldBtn?.text =
                Utils.epochMilliToLocalDate(searchViewModel.selectedSoldDate)
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        }
    }

}