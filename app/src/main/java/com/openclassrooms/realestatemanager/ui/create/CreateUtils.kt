package com.openclassrooms.realestatemanager.ui.create

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.text.Editable
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import java.io.IOException

class CreateUtils {

    companion object {

        fun validPriceText(text: Editable?): String? {
            if (text!!.isEmpty()) {
                return "Can't be empty"
            }
            if(text.matches(regex = "[A-Z]".toRegex())) {
                return "Must end with an lowercase letter "
            }

            if(text.matches(regex = ".*\\d.*".toRegex())) {
                return "no number allowed"
            }

            //if (!text.matches(regex = "[A-Z][a-z]+((\\s?[A-Z][a-z]+){1,2})?".toRegex())) {
            if (!text.matches(regex = "[A-Z][a-z]+((\\s?[A-Z][a-z]+){0,2})?".toRegex())) {
                return "Must start with an uppercase letter "
            }

            return null
        }




        fun savePhotoToInternalMemory(context : Context, filename: String, bmp: Bitmap): Boolean {
            return try {
                context?.openFileOutput("$filename.jpg", Activity.MODE_PRIVATE).use { stream ->

                    //compress photo
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                        throw IOException("erreur compression")
                    }





                }
                true

            } catch (e: IOException) {
                e.printStackTrace()
                false

            }
        }



    }


}