package com.openclassrooms.realestatemanager.ui.create

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.databinding.ItemRealEstatePhotoCreateBinding
import com.openclassrooms.realestatemanager.databinding.ItemRealEstateVideoBinding
import com.openclassrooms.realestatemanager.models.RealEstatePhoto

class AdapterRealEstateAdd(
    private val photoList: List<RealEstatePhoto>,
  //  private val videoList:List<RealEstateVideo>,
    callback: InterfacePhotoTitleChanged,
    context: Context
) : RecyclerView.Adapter<AdapterRealEstateAdd.ViewHolder>() {

    //callback
    private var callback: InterfacePhotoTitleChanged? = callback

    interface InterfacePhotoTitleChanged {
        //method here...
        fun onChangedTitlePhoto(title: String, uri: String?)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = photoList[position]

//        val video = videoList[position]

        Glide.with(holder.image)
            .load(item.uri)
            .centerCrop()
            .into(holder.image)

        holder.title.addTextChangedListener {
            callback?.onChangedTitlePhoto(holder.title.text.toString(), photoList[position].uri)
        }


        //remove it after test
      //  holder.videoUri.text = video.uri




    }

    override fun getItemCount(): Int {
        return photoList.size
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRealEstatePhotoCreateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        if (viewType == 0){
            val binding = ItemRealEstateVideoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        }

        return ViewHolder(binding)
    }


    override fun getItemViewType(position: Int): Int {

        if(photoList.get(position).uri?.contains("photo")!!){
            return 0
        }else
        {
            return 1
        }

        return super.getItemViewType(position)
    }


    //holder view
    inner class ViewHolder(binding: ItemRealEstatePhotoCreateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var title: EditText = binding.photoTitle
        val image: ImageView = binding.imageview
        val videoUri : TextView = binding.uriVideo
    }

    //holder view
    inner class ViewHolderVideo(binding: ItemRealEstateVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val video: VideoView = binding.videoView

    }
}