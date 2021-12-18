package com.openclassrooms.realestatemanager.ui.create

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.RealEstateMedia

class CreateAdapter(
    val mediaList: List<RealEstateMedia>,
    callback: InterfacePhotoTitleChanged
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //**********************************************************************************************
    private var callback: InterfacePhotoTitleChanged? = callback

    interface InterfacePhotoTitleChanged {
        //method here...
        fun onChangedTitlePhoto(title: String, uri: String)
        fun onDeletePhoto(media: RealEstateMedia)
    }
    //**********************************************************************************************


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
                callback?.onChangedTitlePhoto(photoTitle.text.toString(), photoModel.uri!!)
            }

            photoTitle.setText(photoModel.name)

            delete.setOnClickListener {
                callback?.onDeletePhoto(photoModel)
            }


        }
    }

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(videoModel: RealEstateMedia) {

            itemView.findViewById<TextView>(R.id.video_title).text = videoModel.name

            // val imageMask: ImageView = itemView.findViewById(R.id.image_view_mask)
            val video: VideoView = itemView.findViewById(R.id.video_view_add)
            val delete = itemView.findViewById<ImageView>(R.id.delete_btn)

            val videoTitle: EditText = itemView.findViewById(R.id.video_title)

            videoTitle.addTextChangedListener {
                callback?.onChangedTitlePhoto(videoTitle.text.toString(), videoModel.uri!!)
            }

            delete.setOnClickListener {
                callback?.onDeletePhoto(videoModel)
            }


            videoTitle.setText(videoModel.name)

            video.setVideoURI(videoModel.uri?.toUri())
//            Glide.with(itemView)
//                .load(videoModel.uri)
//                .centerCrop()
//                .into(imageMask)

            //add listener on image view mask
//            imageMask.setOnClickListener {
//
//                imageMask.isVisible = false
//                video.setVideoURI(videoModel.uri?.toUri())
//                video.start()
//                video.isVisible = true
//
//                video.setOnFocusChangeListener { view, b ->
//                    view.isVisible = false
//                    imageMask.isVisible = true
//                }
//
//                val videoTitle: EditText = itemView.findViewById(R.id.video_title)
//
//                videoTitle.addTextChangedListener {
//                    callback?.onChangedTitlePhoto(videoTitle.text.toString(), videoModel.uri!!)
//                }
//
//            }

            video.setOnClickListener(View.OnClickListener {
                video.start()
            })

            video.setOnCompletionListener {
                video.start()
                video.pause()
            }

            video.setOnFocusChangeListener { view, b ->
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