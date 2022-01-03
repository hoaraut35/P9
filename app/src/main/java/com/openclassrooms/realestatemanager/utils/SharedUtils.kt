package com.openclassrooms.realestatemanager.utils

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import java.io.IOException

//kotlin class to share function...

class SharedUtils {

    companion object {

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
                    .setContentTitle(task)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(desc))
                    .setDefaults(Notification.DEFAULT_SOUND)
                        //250x250 else crash
                    .setSmallIcon(R.drawable.ic_baseline_real_estate_app)
            manager.notify(1, builder.build())
        }

        fun savePhotoToInternalMemory(context: Context,filename: String, bmp: Bitmap): Boolean {
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