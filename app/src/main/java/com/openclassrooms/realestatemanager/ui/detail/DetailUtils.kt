package com.openclassrooms.realestatemanager.ui.detail

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DetailUtils {

    companion object {

        fun savePhotoToInternalMemory(
            fileDate: String,
            fileName: String,
            bitmap: Bitmap,
            context: Context,
        ): Boolean {
            return try {
                context.openFileOutput("$fileName$fileDate.jpg", Activity.MODE_PRIVATE)
                    .use { stream ->

                        if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                            throw IOException("error compression")
                        }
                    }
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }

        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("dd.MM.yyyy HH:mm")
            return format.format(date)
        }

    }
}