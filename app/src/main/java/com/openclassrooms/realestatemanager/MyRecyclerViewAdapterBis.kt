package com.openclassrooms.realestatemanager

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.databinding.ItemRealEstateBinding
import com.openclassrooms.realestatemanager.models.RealEstate

class MyRecyclerViewAdapterBis(
    private val values: List<RealEstate>,
    private val onClickListener: View.OnClickListener
) : RecyclerView.Adapter<MyRecyclerViewAdapterBis.ViewHolder>() {

    //create view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRealEstateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    //load data into view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.type.text = item.typeOfProduct
        holder.city.text = item.cityOfProduct
        holder.price.text = item.price.toString()

        val avatar : String = "https://eu.ui-avatars.com/api/?name=test"

        Glide.with(holder.itemView)
            .load(avatar)
            .centerCrop()
            .into(holder.image)

        holder.itemView.setOnClickListener {
            Log.i("[THOMAS]", "click")
            onClickListener
        }

    }

    //data length
    override fun getItemCount(): Int {
        return values.size
    }

    //holder view
    inner class ViewHolder(binding: ItemRealEstateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val type: TextView = binding.typeText
        val price: TextView = binding.priceText
        val city: TextView = binding.cityText
        val image: ImageView = binding.realEstateImage
    }


}