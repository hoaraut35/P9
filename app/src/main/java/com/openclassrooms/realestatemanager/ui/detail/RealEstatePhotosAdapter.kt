package com.openclassrooms.realestatemanager.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R

class RealEstatePhotosAdapter(private val realEstatePhotos: List<String>) :
    RecyclerView.Adapter<RealEstatePhotosAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textView: TextView

        init {
            textView = view.findViewById(R.id.photo_title)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_real_estate_photo, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = realEstatePhotos[position]

        holder.textView.text = photo


    }

    override fun getItemCount(): Int = realEstatePhotos.size

}
