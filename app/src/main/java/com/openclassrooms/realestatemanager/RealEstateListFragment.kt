package com.openclassrooms.realestatemanager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.databinding.FragmentListRealestateBinding
import com.openclassrooms.realestatemanager.databinding.ItemRealEstateBinding
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



@AndroidEntryPoint
class RealEstateListFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null





    private var _binding: FragmentListRealestateBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            Log.i("[THOMAS]","sur le fragment list")
        val recyclerView: RecyclerView = binding.recyclerview



        setupRecyclerView(recyclerView)

    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val myList: List<String> =
            listOf("a", "b", "c", "a", "b", "c", "a", "b", "c", "a", "b", "c")
        lateinit var linearLayoutManager: LinearLayoutManager

        linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = MyRecyclerViewAdapter(myList)
    }

    class MyRecyclerViewAdapter( private val values: List<String> ) : RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemRealEstateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.city.text = "item"
        }

        override fun getItemCount(): Int {
            Log.i("[THOMAS]","Taille liste ${values.size}")
            return values.size
        }

        inner class ViewHolder(binding: ItemRealEstateBinding) : RecyclerView.ViewHolder(binding.root) {

            val type: TextView = binding.typeText
            val price: TextView = binding.priceText
            val city: TextView = binding.cityText

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
}