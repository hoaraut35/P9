package com.openclassrooms.realestatemanager.ui.addupdate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView

import com.openclassrooms.realestatemanager.databinding.ItemRealEstatePhotoBinding
import com.openclassrooms.realestatemanager.models.RealEstate

class MyNewAddPhotoVideoAdapter(
    private val media: List<RealEstate>
) : RecyclerView.Adapter<MyNewAddPhotoVideoAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


    }

    //data length
    override fun getItemCount(): Int {
        return media.size
    }


    //holder view
    inner class ViewHolder(binding: ItemRealEstatePhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

      //  val type: TextView = binding.typeText
      //  val price: TextView = binding.priceText
      //  val city: TextView = binding.cityText
      //  val image: ImageView = binding.realEstateImage
      //  val layoutContainer: LinearLayoutCompat = binding.layoutContainer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRealEstatePhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


}