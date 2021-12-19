package com.openclassrooms.realestatemanager.ui.update

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import java.io.IOException

class UpdateUtils {

    companion object {

        fun deleteMediaFromInternalMemory(context: Context, media: RealEstateMedia) {
            context.deleteFile(media.uri?.substringAfterLast("/"))
        }

        fun saveMediaToInternalMemory(context: Context, fileName: String, bitmap: Bitmap) {

            context.openFileOutput(fileName, Activity.MODE_PRIVATE).use { stream ->

                //create bitmap
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                    throw IOException("Fail to compress")
                }


            }


        }
    }
}