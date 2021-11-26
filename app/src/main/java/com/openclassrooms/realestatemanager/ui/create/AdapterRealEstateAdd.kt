package com.openclassrooms.realestatemanager.ui.create

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.RealEstateMedia

class AdapterRealEstateAdd(
    private val mediaList: List<RealEstateMedia>,
    callback: InterfacePhotoTitleChanged
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //callback
    private var callback: InterfacePhotoTitleChanged? = callback

    interface InterfacePhotoTitleChanged {
        //method here...
        fun onChangedTitlePhoto(title: String, uri: String?)
    }


    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(photoModel: RealEstateMedia) {

          //  itemView.findViewById<EditText>(R.id.photo_title).text = photoModel.name

            val image = itemView.findViewById<ImageView>(R.id.imageview)

            Glide.with(itemView)
                .load(photoModel.uri)
                .centerCrop()
                .into(image)

        }
    }

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(videoModel: RealEstateMedia) {

            itemView.findViewById<TextView>(R.id.video_title).text = videoModel.name

            val video : VideoView = itemView.findViewById(R.id.video_view_add)
                video.setVideoURI(videoModel.uri?.toUri())
                video.start()
        }
    }

    override fun getItemCount(): Int {
        return mediaList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_real_estate_photo_create, parent, false)
            return PhotoViewHolder(view.rootView)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_real_estate_video, parent, false)
            return VideoViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mediaList[position].uri?.contains("/data/user/")!!) {
            0
        } else {
            1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 0) {
            (holder as PhotoViewHolder).bind(mediaList[position])
        } else {
            (holder as VideoViewHolder).bind(mediaList[position])
        }
    }
}