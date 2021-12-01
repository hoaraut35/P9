package com.openclassrooms.realestatemanager.ui.detail

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeTransition

class MyRequestImageListener(private val callback: Callback? = null) : RequestListener<Drawable> {

    interface Callback {
        fun onFailure(message: String?)
        fun onSuccess(dataSource: Drawable?)
    }

    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
    ): Boolean {
        callback?.onFailure(e?.message)
        return false
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        callback?.onSuccess(resource)
        if (resource != null) {
            target?.onResourceReady(resource, DrawableCrossFadeTransition(1000, isFirstResource))
        }
        return true
    }
}