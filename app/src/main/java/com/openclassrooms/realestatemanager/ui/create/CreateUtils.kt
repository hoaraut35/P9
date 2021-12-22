package com.openclassrooms.realestatemanager.ui.create

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CreateUtils {

    companion object{

        fun savePhotoToInternalMemory(filename: String, bmp: Bitmap, context: Context): Boolean {
            return try {
                context?.openFileOutput("$filename.jpg", Activity.MODE_PRIVATE).use { stream ->

                    //compress photo
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                        throw IOException("erreur compression")
                    }

                    val fileNameUri = context?.filesDir.toString() + "/" + filename + ".jpg"




                }
                true

            } catch (e: IOException) {
                e.printStackTrace()
                false

            }
        }
    }
}