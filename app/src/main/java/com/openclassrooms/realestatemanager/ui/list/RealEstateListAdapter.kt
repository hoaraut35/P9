package com.openclassrooms.realestatemanager.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ItemRealEstateBinding
import com.openclassrooms.realestatemanager.models.RealEstateWithMedia
import com.openclassrooms.realestatemanager.utils.Utils
import java.text.NumberFormat
import java.util.*

class RealEstateListAdapter(
    private val realEstateFullData: List<RealEstateWithMedia>,
    private val onClickListener: View.OnClickListener
) : RecyclerView.Adapter<RealEstateListAdapter.ViewHolder>() {

    //private var focusedItem = 0

    //create view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRealEstateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    //load data into view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = realEstateFullData[position]

        //show type of product
        holder.type.text = item.realEstate.typeOfProduct
        //show city of product
        holder.city.text = item.realEstate.cityOfProduct

        //move to utils class
        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.maximumFractionDigits = 0
        currencyFormat.currency = Currency.getInstance("EUR")

        if (item.realEstate.price != null){
            holder.price.text = Utils.getCurrencyFormat().format(item.realEstate.price!!)
        }


//        if (item.photosList[0].uri != null){
//            Glide.with(holder.itemView)
//                .load(item.photosList[0].uri)
//                // .override(100, 100)
//                //.centerInside()
//                .centerCrop() //ok
//                //  .fitCenter()
//                .into(holder.image)
//
//        }else
//        {


            if (item.photosList.isEmpty()){
                Glide.with(holder.itemView)
                    .load(R.drawable.realestate_1)
                    // .override(100, 100)
                    //.centerInside()
                    .centerCrop() //ok
                    //  .fitCenter()
                    .into(holder.image)

            }else
            {
                Glide.with(holder.itemView)
                    .load(item.photosList[0].uri)
                    // .override(100, 100)
                    //.centerInside()
                    .centerCrop() //ok
                    //  .fitCenter()
                    .into(holder.image)

            }


//        }


        with(holder.itemView) {
            tag = item.realEstate.realEstateId
            setOnClickListener(onClickListener)
        }

    }

    //data length
    override fun getItemCount(): Int {        return realEstateFullData.size
    }



    //holder view
    inner class ViewHolder(binding: ItemRealEstateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val type: TextView = binding.typeText
        val price: TextView = binding.priceText
        val city: TextView = binding.cityText
        val image: ImageView = binding.realEstateImage
        val layoutContainer: LinearLayoutCompat = binding.layoutContainer






    }


}