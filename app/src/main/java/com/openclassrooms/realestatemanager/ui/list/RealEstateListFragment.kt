package com.openclassrooms.realestatemanager.ui.list

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.databinding.FragmentListRealestateBinding
import com.openclassrooms.realestatemanager.models.RealEstateFull
import com.openclassrooms.realestatemanager.ui.MainViewModel
import com.openclassrooms.realestatemanager.ui.detail.DetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RealEstateListFragment : Fragment() {

    private val listViewModel by viewModels<ListViewModel>()

    private var _binding: FragmentListRealestateBinding? = null
    private val binding get() = _binding!!

    lateinit var estateList: List<RealEstateFull>

    private var searchInProgress: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        val recyclerView: RecyclerView = binding.recyclerview

        //check if detail fragment exist
        val realEstateDetailFragment: View? = view.findViewById(R.id.item_detail_nav_container)

        //listener set id itemView here for second fragment with bundle
        val onClickListener = View.OnClickListener { realEstateView ->
            //get realEstate id
            val item = realEstateView.tag
            //create bundle
            val bundle = Bundle()
            bundle.putString(DetailFragment.ARG_REAL_ESTATE_ID, item.toString())
            //if fragment detail is displayed mode tablet
            if (realEstateDetailFragment != null) {
                realEstateDetailFragment.findNavController()
                    .navigate(R.id.fragment_item_detail, bundle)
            } else {
                realEstateView.findNavController().navigate(R.id.show_item_detail, bundle)
            }
        }

        //Observe...
        listViewModel.getResultListSearch().observe(viewLifecycleOwner){
            if (!it.isNullOrEmpty()) {
                searchInProgress = true
                setupRecyclerView(recyclerView, it, onClickListener)
                binding.floatingSearch?.show()
            }
        }

        //Observe...
        listViewModel.getRealEstateFull().observe(viewLifecycleOwner){listRealEstate ->
            //save new list to viewmodel...
            estateList = listRealEstate
            if (!searchInProgress) {
                setupRecyclerView(recyclerView, listRealEstate, onClickListener)
            }
        }

        //Listener...
        binding.floatingSearch?.setOnClickListener {
            //clear list...
            listViewModel.clearSearch()
            setupRecyclerView(recyclerView, estateList, onClickListener)
            searchInProgress = false
            binding.floatingSearch?.hide()
        }

    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        myRealEstateList: List<RealEstateFull>,
        onClickListener: View.OnClickListener
    ) {
        val controller : LayoutAnimationController = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_animation_fall_down)
        recyclerView.layoutAnimation = controller
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.scheduleLayoutAnimation()
        recyclerView.adapter = ListAdapter(myRealEstateList, onClickListener)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListRealestateBinding.inflate(inflater, container, false)

        //TODO: update
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        val isMetered = cm.isActiveNetworkMetered()
        Log.i("[NETWORK]", "State : $isConnected $isMetered")

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.realEstateUpdateBtnNew).isVisible = false
    }

}
