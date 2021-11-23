package com.openclassrooms.realestatemanager.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.databinding.ItemRealEstatePhotoBinding
import com.openclassrooms.realestatemanager.models.RealEstatePhoto

class RealEstatePhotosAdapter(private val realEstatePhotos: List<RealEstatePhoto>) :
    RecyclerView.Adapter<RealEstatePhotosAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRealEstatePhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.type.text = realEstatePhotos[position].name

        Glide.with(holder.image)
            .load(realEstatePhotos[position].uri)
            .centerCrop()
            .into(holder.image)

    }

    override fun getItemCount(): Int = realEstatePhotos.size

    //holder view
    inner class ViewHolder(binding: ItemRealEstatePhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val type: TextView = binding.photoTitle
        val image: ImageView = binding.imageview
    }


}
