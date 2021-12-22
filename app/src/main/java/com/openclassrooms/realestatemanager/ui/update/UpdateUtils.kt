package com.openclassrooms.realestatemanager.ui.update

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import com.openclassrooms.realestatemanager.models.RealEstateMedia
import java.io.FileInputStream
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


        fun saveVideoToInternalStorage(filename: String, uri: Uri,context : Context): Boolean {

            return try {
                context?.openFileOutput("$filename", Activity.MODE_PRIVATE).use { stream ->

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

    }
}