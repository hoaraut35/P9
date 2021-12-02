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
import com.openclassrooms.realestatemanager.models.RealEstateWithMedia
import com.openclassrooms.realestatemanager.ui.MainViewModel
import com.openclassrooms.realestatemanager.ui.detail.RealEstateDetailFragment
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint  //Hilt annotation for fragment
class RealEstateListFragment : Fragment() {

    private val mainViewModel by viewModels<MainViewModel>()

    private var _binding: FragmentListRealestateBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true);

        val recyclerView: RecyclerView = binding.recyclerview

        //check if detail fragmenet exist
        val realEstateDetailFragment: View? = view.findViewById(R.id.item_detail_nav_container)

        //listener set id itemview here for second frafgment with bundle
        val onClickListener = View.OnClickListener { realEstateView ->
            //get realEstate id
            val item = realEstateView.tag
            //create bundle
            val bundle = Bundle()
            bundle.putString(RealEstateDetailFragment.ARG_REAL_ESTATE_ID, item.toString())
            //if fragment detail is displayed mode tablet
            if (realEstateDetailFragment != null) {
                realEstateDetailFragment.findNavController()
                    .navigate(R.id.fragment_item_detail, bundle)
            } else {
                realEstateView.findNavController().navigate(R.id.show_item_detail, bundle)
            }
        }

        mainViewModel.allRealEstateWithPhotos.observe(viewLifecycleOwner) { listRealEstateWithPhotos ->
            setupRecyclerView(recyclerView, listRealEstateWithPhotos, onClickListener)
        }

    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        myRealEstateListWithPhotos: List<RealEstateWithMedia>,
        onClickListener: View.OnClickListener
    ) {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = RealEstateListAdapter(myRealEstateListWithPhotos, onClickListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListRealestateBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RealEstateListFragment().apply {     }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.realEstateUpdateBtnNew).isVisible = false
    }

}
