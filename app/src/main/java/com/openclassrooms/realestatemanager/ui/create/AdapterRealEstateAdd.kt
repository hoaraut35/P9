package com.openclassrooms.realestatemanager.ui.create

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.databinding.ItemRealEstatePhotoBinding
import com.openclassrooms.realestatemanager.databinding.ItemRealEstatePhotoCreateBinding

class AdapterRealEstateAdd(
    private val media: List<String>,
    callback :PhotoTitleChanged,
    context : Context) :
    RecyclerView.Adapter<AdapterRealEstateAdd.ViewHolder>() {


    private var callback : PhotoTitleChanged? = callback

    interface PhotoTitleChanged{
        fun onChangedTitlePhoto(title : String)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = media[position]

       //holder.title.text = "Photo $position"

        Glide.with(holder.image)
            .load(item)
            .centerCrop()
            .into(holder.image)


        holder.title.addTextChangedListener {
           // Log.i("[UPDATE]","changement " + holder.title.text.toString())
            callback?.onChangedTitlePhoto(holder.title.text.toString())
        }
    }

    //data length
    override fun getItemCount(): Int {
        return media.size
    }


    //holder view
    inner class ViewHolder(binding: ItemRealEstatePhotoCreateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var title: EditText = binding.photoTitle
        val image: ImageView = binding.imageview
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRealEstatePhotoCreateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


}