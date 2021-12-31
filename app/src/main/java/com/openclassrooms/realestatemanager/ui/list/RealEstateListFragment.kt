package com.openclassrooms.realestatemanager.ui.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentListRealestateBinding
import com.openclassrooms.realestatemanager.models.RealEstateFull
import com.openclassrooms.realestatemanager.ui.detail.DetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RealEstateListFragment : Fragment() {

    private val listViewModel by viewModels<ListViewModel>()

    private var _binding: FragmentListRealestateBinding? = null
    private val binding get() = _binding!!

    private lateinit var estateList: List<RealEstateFull>
    private var searchInProgress: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        val recyclerView: RecyclerView = binding.recyclerview

        //check if the detail fragment exist
        val realEstateDetailFragment: View? = view.findViewById(R.id.item_detail_nav_container)

        //listener set id itemView here for second fragment with bundle
        val onClickListener = View.OnClickListener { realEstateView ->
            val bundle = Bundle()
            bundle.putString(DetailFragment.ARG_REAL_ESTATE_ID, realEstateView.tag.toString())

            //if the detail fragment is displayed ...
            if (realEstateDetailFragment != null) {
                realEstateDetailFragment.findNavController().navigate(R.id.fragment_item_detail, bundle)

            } else {
                //we open the detail fragment
                realEstateView.findNavController().navigate(R.id.show_item_detail, bundle)
            }
        }

        //Observe if we have a search in progress...
        listViewModel.getResultListSearch().observe(viewLifecycleOwner){resultSearch ->
            if (!resultSearch.isNullOrEmpty()) {
                searchInProgress = true
                setupRecyclerView(recyclerView, resultSearch, onClickListener)
                binding.floatingSearch.show()
            }
        }

        //Observe the full list of estate...
        listViewModel.getRealEstateFull().observe(viewLifecycleOwner){listRealEstate ->
            //save this list to viewModel...
            estateList = listRealEstate
            if (!searchInProgress) {
                setupRecyclerView(recyclerView, listRealEstate, onClickListener)
            }
        }

        //Listener...
        binding.floatingSearch.setOnClickListener {
            //clear list...
            listViewModel.clearSearch()
            setupRecyclerView(recyclerView, estateList, onClickListener)
            searchInProgress = false
            binding.floatingSearch.hide()
        }

    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        myRealEstateList: List<RealEstateFull>,
        onClickListener: View.OnClickListener
    ) {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = ListAdapter(myRealEstateList, onClickListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListRealestateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.findItem(R.id.realEstateUpdateBtnNew).isVisible = false
    }

}
