package com.openclassrooms.realestatemanager.utils

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.text.Editable
import androidx.core.app.NotificationCompat
import com.openclassrooms.realestatemanager.R
import java.io.IOException

class SharedUtils {

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
                context?.openFileOutput("$filename", Activity.MODE_PRIVATE).use { stream ->

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

        fun notification(task: String, desc: String, context: Context) {
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel =
                    NotificationChannel(
                        "realEstate",
                        "realEstate",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                manager.createNotificationChannel(channel)
            }
            val builder: NotificationCompat.Builder =
                NotificationCompat.Builder(context, "realEstate")
                    .setContentTitle("RealEsatate")
                    .setStyle(NotificationCompat.BigTextStyle().bigText("Sauvegarde terminée"))
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setSmallIcon(R.mipmap.ic_launcher)
            manager.notify(1, builder.build())
        }

        fun savePhotoToInternalMemory(filename: String, bmp: Bitmap, context : Context): Boolean {
            return try {
                context?.openFileOutput("$filename", Activity.MODE_PRIVATE).use { stream ->
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