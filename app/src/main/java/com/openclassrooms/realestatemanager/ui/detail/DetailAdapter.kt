package com.openclassrooms.realestatemanager.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.databinding.ItemRealEstatePhotoBinding
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import com.openclassrooms.realestatemanager.ui.create.CreateAdapter

class DetailAdapter(
    private val realEstateMedias: List<RealEstateMedia>,
    callback: InterfacePhotoFullScreen) :
    RecyclerView.Adapter<DetailAdapter.ViewHolder>() {

    //**********************************************************************************************
    private var callback: InterfacePhotoFullScreen? = callback

    interface InterfacePhotoFullScreen {
        fun onViewFullScreenMedia(title: String, uri: String)
    }
    //**********************************************************************************************


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRealEstatePhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.type.text = realEstateMedias[position].name

        Glide.with(holder.image)
            .load(realEstateMedias[position].uri)
            .centerCrop()
            .into(holder.image)


        holder.itemView.setOnClickListener {
            callback?.onViewFullScreenMedia(realEstateMedias[position].name.toString(), realEstateMedias[position].uri!!)
        }

    }

    override fun getItemCount(): Int = realEstateMedias.size

    //holder view
    inner class ViewHolder(binding: ItemRealEstatePhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val type: TextView = binding.photoTitle
        val image: ImageView = binding.imageview
    }

}
