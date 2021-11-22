package com.openclassrooms.realestatemanager.ui.detail

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.databinding.ItemRealEstatePhotoBinding
import com.openclassrooms.realestatemanager.models.RealEstateWithPhotos

class RealEstatePhotosAdapter(private val realEstatePhotos: List<RealEstateWithPhotos>) :
    RecyclerView.Adapter<RealEstatePhotosAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRealEstatePhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val realEstatePhoto = realEstatePhotos[position]

        Log.i("[RECYCLERVIEW]","" + realEstatePhoto.photosList.size.toString())

        for (item in realEstatePhoto.photosList){
            holder.type.text = item.name
        }








    }

    override fun getItemCount(): Int = realEstatePhotos.size


    //holder view
    inner class ViewHolder(binding: ItemRealEstatePhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val type: TextView = binding.photoTitle

       // val layoutContainer: LinearLayoutCompat = binding.layoutContainer




    }


}
