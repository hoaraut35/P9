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

        binding.priceRange.setLabelFormatter {
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("USD")
            format.format(it.toDouble())
        }

        if (searchViewModel.minPrice != null){
            binding.minPrice.text = searchViewModel.minPrice.toString()
        }else
        {
            binding.minPrice.text = Utils.getCurrencyFormat().format(binding.priceRange.values[0].toInt())
        }

        if(searchViewModel.maxPrice != null){
            binding.maxPrice.text = searchViewModel.maxPrice.toString()
        }else{
            binding.maxPrice.text = Utils.getCurrencyFormat().format(binding.priceRange.values[1].toInt())
        }

        if(searchViewModel.minSurface != null){
            binding.minSurface.text = searchViewModel.minSurface.toString().plus("m²")
        }else
        {
            binding.minSurface.text = binding.surfaceRange.values[0].toInt().toString().plus("m²")
        }

        if (searchViewModel.maxPrice != null){
            binding.maxSurface.text = searchViewModel.maxPrice.toString().plus("m²")
        }else{
            binding.maxSurface.text = binding.surfaceRange.values[1].toInt().toString().plus("m²")
        }

        //price listener
        val priceListener: RangeSlider.OnSliderTouchListener = object :
            RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                searchViewModel.minPrice = slider.values[0].toInt()
                searchViewModel.maxPrice = slider.values[1].toInt()
                binding.minPrice.text =  Utils.getCurrencyFormat().format(slider.values[0].toInt())
                binding.maxPrice.text = Utils.getCurrencyFormat().format(slider.values[1].toInt())
            }
        }

        binding.priceRange.addOnSliderTouchListener(priceListener)

        //surface label
        binding.surfaceRange.setLabelFormatter {
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

                binding.minSurface.text = slider.values[0].toString().plus("m²")
                binding.maxSurface.text = slider.values[1].toString().plus("m²")

            }
        }
        binding.surfaceRange.addOnSliderTouchListener(surfaceListener)

        //media number listener
        val mediaNumberListener: Slider.OnSliderTouchListener = object :
            Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
            }

            override fun onStopTrackingTouch(slider: Slider) {
                searchViewModel.numberOfMedia = slider.value.toInt()
                binding.mediaNumberView.text = slider.value.toString()
            }
        }
        binding.mediaNumber.addOnSliderTouchListener(mediaNumberListener)

        //search button listener
        binding.searchBtn.setOnClickListener {

            Log.i("[SQL]", SearchUtils.checkSQLiteVersion())

            Toast.makeText(requireContext(), "Search in progress...", Toast.LENGTH_SHORT).show()

            var schoolState = false
            var station = false
            var park = false

            var intSchoolState = 0
            var intStation = 0
            var intPark = 0

            val pointsOfInterestChipGroup: ChipGroup = binding.chipRealEstatePoi

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

                intSchoolState = if (schoolState) {
                    1
                } else {
                    0
                }

                intStation = if (station) {
                    1
                } else {
                    0
                }

                intPark = if (park) {
                    1
                } else {
                    0
                }

            }

            var queryString = ""
            val args = mutableListOf<Any>()
            var containsCondition = false

            queryString += "SELECT * FROM realEstate_table"

            if (searchViewModel.numberOfMedia != null) {
                queryString += " INNER JOIN RealEstateMedia ON RealEstateMedia.realEstateParentId = realEstate_table.realEstateId"
            }

            queryString += " INNER JOIN RealEstatePOI ON RealEstatePOI.realEstateParentId = realEstate_table.realEstateId"

            //ok
            if (searchViewModel.maxPrice != null && searchViewModel.minPrice != null) {
                containsCondition = true
                queryString += " WHERE realEstate_table.price BETWEEN ${searchViewModel.minPrice} AND ${searchViewModel.maxPrice}"
            }

            //ok
            if (searchViewModel.maxSurface != null && searchViewModel.minSurface != null) {
                if (containsCondition) {
                    queryString += " AND "
                } else {
                    queryString += " WHERE"
                    containsCondition = true
                }
                queryString += " realEstate_table.surface BETWEEN ${searchViewModel.minSurface} AND ${searchViewModel.maxSurface}"
            }

            if (searchViewModel.selectedEntryDate != null) {
                if (containsCondition) {
                    queryString += " AND "
                } else {
                    queryString += " WHERE"
                    containsCondition = true
                }
                queryString += " realEstate_table.dateOfEntry >= '${searchViewModel.selectedEntryDate}'"
            }

            if (searchViewModel.selectedSoldDate != null) {
                if (containsCondition) {
                    queryString += " AND "
                } else {
                    queryString += " WHERE"
                }
                queryString += " property_table.dateOfSale >= '${searchViewModel.selectedSoldDate}'"
            }

            if (searchViewModel.numberOfMedia != null) {
                queryString += " GROUP BY RealEstateMedia.realEstateParentId,"
                queryString += " RealEstatePOI.realEstateParentId HAVING COUNT(RealEstateMedia.realEstateParentId) >= ${searchViewModel.numberOfMedia} "+
                 " AND RealEstatePOI.park >= $intPark AND RealEstatePOI.school >= $intSchoolState AND RealEstatePOI.station >= $intStation"
            } else {
                queryString += " GROUP BY RealEstatePOI.realEstateParentId HAVING RealEstatePOI.park >= $intPark AND RealEstatePOI.school >= $intSchoolState " +
                        "AND RealEstatePOI.station >= $intStation"
            }

            queryString += ";"

            //show query in log...
            //Log.i("[SQL]", "My query : $queryString")

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

                    it.forEach { mySearchResult ->
                        Log.i("[SQL]", "data" + mySearchResult.realEstateFullData.typeOfProduct)
                        closeFragment()
                    }
                }

            }

        }

        binding.dateBtn.setOnClickListener {
            createDatePicker(it as TextView, childFragmentManager)
        }

        binding.dateSoldBtn.setOnClickListener {
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

        if (mDatePicker == null || !mDatePicker.isAdded) {
            mDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select a date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            mDatePicker.addOnPositiveButtonClickListener {
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
            binding.dateBtn.text = Utils.epochMilliToLocalDate(searchViewModel.selectedEntryDate)
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        }

        if (searchViewModel.selectedSoldDate != null) {
            binding.dateSoldBtn.text =
                Utils.epochMilliToLocalDate(searchViewModel.selectedSoldDate)
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        }
    }

}