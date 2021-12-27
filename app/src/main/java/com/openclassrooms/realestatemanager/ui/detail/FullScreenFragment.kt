package com.openclassrooms.realestatemanager.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.openclassrooms.realestatemanager.R

class FullScreenFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fullscreen_media, container, false)
        val myArgs = arguments
        val uri = myArgs?.getString("uri")
        val video: VideoView = rootView.findViewById(R.id.videoView)
        val image: ImageView = rootView.findViewById(R.id.imageView)
        val playBtn: FloatingActionButton = rootView.findViewById(R.id.play_btn)
        val stopBtn : FloatingActionButton = rootView.findViewById(R.id.stop_btn)

        if (uri?.contains("Photo") == true) {
            image.isVisible = true
            video.isVisible = false

            Glide.with(image)
                .load(uri)
                .centerCrop()
                .into(image)

        } else {
            image.isVisible = false
            video.isVisible = true

            video.setVideoURI(uri?.toUri())
            val mediaC = MediaController(requireContext())
            mediaC.setAnchorView(video)
            video.setMediaController(mediaC)

            video.setOnPreparedListener {
                video.start()
            }

            playBtn.setOnClickListener { stopBtn.show()
                playBtn.hide()
                video.start()
            }

            stopBtn.setOnClickListener {
                playBtn.show()
                stopBtn.hide()
                video.pause()
            }

            video.setOnCompletionListener {
                if(it.isPlaying){
                    playBtn.hide()
                    stopBtn.show()
                }else{
                    stopBtn.hide()
                    playBtn.show()
                }
            }

        }

        return rootView
    }

}