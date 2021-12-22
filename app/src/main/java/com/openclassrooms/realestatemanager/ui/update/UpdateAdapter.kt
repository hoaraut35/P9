package com.openclassrooms.realestatemanager.ui.update

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.VideoView
import androidx.core.net.toUri
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
                    callback?.onToast("Gardez au moins un media")
                }

            }
        }
    }

    //for video...
    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(videoModel: RealEstateMedia) {

            val video: VideoView = itemView.findViewById(R.id.video_view_add)
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




            video.setVideoURI(videoModel.uri?.toUri())

            video.setOnClickListener(View.OnClickListener {
                video.start()
            })

            video.setOnCompletionListener {
                video.start()
                video.pause()
            }

            video.setOnFocusChangeListener { _, _ ->
                video.start()
                video.pause()
            }

            video.setOnPreparedListener {
                video.start()
                video.pause()
            }

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