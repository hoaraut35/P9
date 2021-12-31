package com.openclassrooms.realestatemanager.ui.update


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

class UpdateAdapter(
    val mediaList: List<RealEstateMedia>,
    callback: InterfaceMediaAdapter
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var callback: InterfaceMediaAdapter? = callback

    interface InterfaceMediaAdapter {
        fun onViewFullScreenMedia(title: String, uri: String)
        fun onChangedTitleMedia(title: String, uri: String)
        fun onDeleteMedia(media: RealEstateMedia)
        fun onToast(text: String)
    }

    //for photo...
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
                if (mediaList.size > 1) {
                    callback?.onDeleteMedia(photoModel)
                } else {
                    callback?.onToast("no_delete")
                }

            }

            image.setOnClickListener {
                callback?.onViewFullScreenMedia(photoModel.name.toString(), photoModel.uri!!)
            }

        }
    }

    //for video...
    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(videoModel: RealEstateMedia) {

            val video: ImageView = itemView.findViewById(R.id.video_view_add)
            val delete = itemView.findViewById<ImageView>(R.id.delete_btn)
            val videoTitle: EditText = itemView.findViewById(R.id.video_title)

            videoTitle.addTextChangedListener {
                callback?.onChangedTitleMedia(videoTitle.text.toString(), videoModel.uri!!)
            }

            videoTitle.setText(videoModel.name)

            delete.setOnClickListener {
                if (mediaList.size > 1) {
                    callback?.onDeleteMedia(videoModel)
                } else {
                    callback?.onToast("Gardez au moins un media")
                }

            }

            Glide.with(itemView)
                .load(videoModel.uri)
                .centerCrop()
                .into(video)

            video.setOnClickListener{
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

    override fun getItemViewType(position: Int): Int {
        return if (mediaList[position].uri?.contains("Photo")!!) {
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