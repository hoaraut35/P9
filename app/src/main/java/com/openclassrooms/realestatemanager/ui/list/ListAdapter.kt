package com.openclassrooms.realestatemanager.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ItemRealEstateBinding
import com.openclassrooms.realestatemanager.models.RealEstateFull
import com.openclassrooms.realestatemanager.utils.Utils
import java.text.NumberFormat
import java.util.*

class ListAdapter(
    private val realEstateFullData: List<RealEstateFull>,
    private val onClickListener: View.OnClickListener
) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRealEstateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = realEstateFullData[position]

        holder.type.text = item.realEstateFullData.typeOfProduct
        holder.city.text = item.realEstateFullData.address?.city

        //TODO: move to utils class
        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.maximumFractionDigits = 0
        currencyFormat.currency = Currency.getInstance("EUR")

        if (item.realEstateFullData.price != null) {
            holder.price.text = Utils.getCurrencyFormat().format(item.realEstateFullData.price!!)
        }

        if (item.mediaList!!.isEmpty()) {
            Glide.with(holder.itemView)
                .load(R.drawable.realestate_1)
                .centerCrop()
                .into(holder.image)
        } else {
            Glide.with(holder.itemView)
                .load(item.mediaList[0].uri)
                .centerCrop()
                .into(holder.image)
        }

        with(holder.itemView) {
            tag = item.realEstateFullData.realEstateId
            setOnClickListener(onClickListener)
        }

    }

    override fun getItemCount(): Int {
        return realEstateFullData.size
    }

    inner class ViewHolder(binding: ItemRealEstateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val type: TextView = binding.typeText
        val price: TextView = binding.priceText
        val city: TextView = binding.cityText
        val image: ImageView = binding.realEstateImage

    }

}