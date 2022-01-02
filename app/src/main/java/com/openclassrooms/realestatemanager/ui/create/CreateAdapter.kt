package com.openclassrooms.realestatemanager.ui.create

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.RealEstateMedia

class CreateAdapter(
    private val mediaList: List<RealEstateMedia>,
    callback: InterfacePhotoTitleChanged
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var callback: InterfacePhotoTitleChanged? = callback

    interface InterfacePhotoTitleChanged {
        fun onViewFullScreenMedia(title: String, uri: String)
        fun onChangedTitleMedia(title: String, uri: String)
        fun onDeletePhoto(media: RealEstateMedia)
    }

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(photoModel: RealEstateMedia) {

            val image = itemView.findViewById<ImageView>(R.id.imageview)
            val delete = itemView.findViewById<ImageView>(R.id.delete_btn)

            Glide.with(itemView)
                .load(photoModel.uri)
                .centerCrop()
                .into(image)

            val photoTitle: EditText = itemView.findViewById(R.id.photo_title)

            photoTitle.addTextChangedListener {
                callback?.onChangedTitleMedia(photoTitle.text.toString(), photoModel.uri!!)
            }

            photoTitle.setText(photoModel.name)

            delete.setOnClickListener {
                callback?.onDeletePhoto(photoModel)
            }

            itemView.setOnClickListener {
                callback?.onViewFullScreenMedia(photoModel.name.toString(), photoModel.uri!!)
            }

        }
    }

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(videoModel: RealEstateMedia) {

            val videoTitle: EditText = itemView.findViewById(R.id.video_title)
            val video: ImageView = itemView.findViewById(R.id.video_view_add)
            val delete = itemView.findViewById<ImageView>(R.id.delete_btn)

            videoTitle.addTextChangedListener {
                callback?.onChangedTitleMedia(videoTitle.text.toString(), videoModel.uri!!)
            }

            delete.setOnClickListener {
                callback?.onDeletePhoto(videoModel)
            }

            videoTitle.setText(videoModel.name)

            Glide.with(itemView)
                .load(videoModel.uri)
                .centerCrop()
                .into(video)

            itemView.setOnClickListener {
                callback?.onViewFullScreenMedia(videoModel.name.toString(), videoModel.uri!!)
            }

        }
    }

    override fun getItemCount(): Int {
        return mediaList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_real_estate_photo_create, parent, false)
            PhotoViewHolder(view.rootView)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_real_estate_video, parent, false)
            VideoViewHolder(view)
        }
    }

    @SuppressLint("SdCardPath")
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