package com.openclassrooms.realestatemanager.ui.create

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.databinding.ItemRealEstatePhotoEditorBinding
import com.openclassrooms.realestatemanager.models.RealEstatePhoto

class AdapterRealEstateAdd(private val media: List<RealEstatePhoto>) :
    RecyclerView.Adapter<AdapterRealEstateAdd.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val myProperty = media[position]

        //holder.titre.setText().text = myProperty.name
        Glide.with(holder.image)
            .load(myProperty.uri)
            .centerCrop()
            .into(holder.image)
    }

    //data length
    override fun getItemCount(): Int {
        return media.size
    }


    //holder view
    inner class ViewHolder(binding: ItemRealEstatePhotoEditorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val titre: EditText = binding.photoTitle

        val image: ImageView = binding.imageview

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRealEstatePhotoEditorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


}