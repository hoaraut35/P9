package com.openclassrooms.realestatemanager.ui.list

import android.util.SparseBooleanArray
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
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.utils.Utils
import java.text.NumberFormat
import java.util.*

class MyRecyclerViewAdapterBis(
    private val values: List<RealEstate>,
    private val onClickListener: View.OnClickListener
) : RecyclerView.Adapter<MyRecyclerViewAdapterBis.ViewHolder>() {

    private var focusedItem = 0

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


        holder.itemView.elevation = 0f



        //TODO: move ti utils class
        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.maximumFractionDigits = 0
        currencyFormat.currency = Currency.getInstance("EUR")

        //show price with locale data
        holder.price.text = Utils.getCurrencyFormat().format(item.price)





            //.setBackgroundColor(Color.parseColor("#80FFFFFF"))

        // Uri uri = Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/real")

        Glide.with(holder.itemView)
            .load(R.drawable.realestate_1)
            // .override(100, 100)
            //.centerInside()
            .centerCrop() //ok
            //  .fitCenter()
            .into(holder.image)



        with(holder.itemView) {
            tag = item.realEstateId
            setOnClickListener(onClickListener)

            val previousItem = focusedItem
            focusedItem = position


           // notifyItemChanged(previousItem)
           // notifyItemChanged(focusedItem)

          //  holder.itemView.elevation=10.0f
            // holder.layoutContainer.setBackgroundColor(Color.parseColor("#664411"))

        }

    }

    //data length
    override fun getItemCount(): Int {        return values.size
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