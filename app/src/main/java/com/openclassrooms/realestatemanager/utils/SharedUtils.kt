package com.openclassrooms.realestatemanager.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.openclassrooms.realestatemanager.R

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
                    .setSmallIcon(R.mipmap.ic_launcher)
            manager.notify(1, builder.build())
        }

    }

}