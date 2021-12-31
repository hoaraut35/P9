package com.openclassrooms.realestatemanager.ui.update

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import java.io.FileInputStream
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class UpdateUtils {

    companion object {

        fun deleteMediaFromInternalMemory(context: Context, media: RealEstateMedia) {
            context.deleteFile(media.uri?.substringAfterLast("/"))
        }

        fun saveVideoToInternalStorage(filename: String, uri: Uri,context : Context): Boolean {

            return try {
                context.openFileOutput(filename, Activity.MODE_PRIVATE).use { stream ->

                    val sourceFile = FileInputStream(
                        context.contentResolver.openFileDescriptor(
                            uri,
                            "r"
                        )?.fileDescriptor
                    )
                    val buf = ByteArray(1024)
                    var len: Int

                    while (sourceFile.read(buf).also { len = it } > 0) {
                        stream?.write(buf, 0, len)
                    }

                    stream?.flush()
                    stream?.close()

                }


                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }


        fun savePhotoToInternalMemory(filename: String, bmp: Bitmap, context : Context): Boolean {
            return try {
                context.openFileOutput(filename, Activity.MODE_PRIVATE).use { stream ->
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                        throw IOException("Error compression")
                    }
                }
                true

            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }



        fun getTodayDate(): String? {
            @SuppressLint("SimpleDateFormat") val dateFormat: DateFormat =
                SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault())
            return dateFormat.format(Date())
        }

    }
}