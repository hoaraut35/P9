package com.openclassrooms.realestatemanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.databinding.FragmentListRealestateBinding
import com.openclassrooms.realestatemanager.databinding.ItemRealEstateBinding
import com.openclassrooms.realestatemanager.models.RealEstate
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


@AndroidEntryPoint  //Hilt annotation for fragment
class RealEstateListFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //viewmodel
    private val mainViewModel by viewModels<MainViewModel>()

    //binding
    private var _binding: FragmentListRealestateBinding? = null
    private val binding get() = _binding!!

    //get data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    //startup
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //bind recyclerview
        val recyclerView: RecyclerView = binding.recyclerview



        //check if detail exist
        val realEstateDetailFragment: View? = view.findViewById(R.id.item_detail_nav_container)

        //listener set id itemview here for second frafgment with bundle
        val onClickListener = View.OnClickListener { realEstateView ->
            val item = realEstateView.tag
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

        //for test
        mainViewModel.allRealEstate.observe(viewLifecycleOwner) { listRealEstate ->
            setupRecyclerView(recyclerView, listRealEstate, onClickListener)

            //  Log.i("[THOMAS]", "recup : ${listRealEstate.size}")
        }

    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        myRealEstateList: List<RealEstate>,
        onClickListener: View.OnClickListener
    ) {



        recyclerView.layoutManager = LinearLayoutManager(activity)


        recyclerView.adapter = SimpleItemRecyclerViewAdapter(myRealEstateList, onClickListener)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListRealestateBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RealEstateListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    class SimpleItemRecyclerViewAdapter(private val realEstateResults: List<RealEstate>,private val onClickListener: View.OnClickListener ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        //create the views but not data
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            val binding =
                ItemRealEstateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)

        }

        //link data and view
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val item = realEstateResults[position]
            holder.type.text = item.typeOfProduct
            holder.city.text = item.cityOfProduct

            //TODO: move ti utils class
            val currencyFormat = NumberFormat.getCurrencyInstance()
            currencyFormat.maximumFractionDigits = 0
            currencyFormat.currency = Currency.getInstance("EUR")

            holder.price.text = currencyFormat.format(item.price)



            // Uri uri = Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/real")

            Glide.with(holder.itemView)
                .load(R.drawable.realestate_1)
               // .override(100, 100)
                //.centerInside()
                .centerCrop() //ok
              //  .fitCenter()
                .into(holder.image)


            with(holder.itemView) {
                tag = item.id
                setOnClickListener(onClickListener)
                // holder.layoutContainer.setBackgroundColor(Color.parseColor("#664411"))

            }


        }

        //get the size of data
        override fun getItemCount() = realEstateResults.size

        //holder view
        inner class ViewHolder(binding: ItemRealEstateBinding) :
            RecyclerView.ViewHolder(binding.root) {

            val type: TextView = binding.typeText
            val price: TextView = binding.priceText
            val city: TextView = binding.cityText
            val image: ImageView = binding.realEstateImage
            val layoutContainer: LinearLayoutCompat = binding.layoutContainer

        }


    }
}
