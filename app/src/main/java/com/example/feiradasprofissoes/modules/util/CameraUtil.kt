package com.example.feiradasprofissoes.modules.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.example.feiradasprofissoes.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

open class CameraUtil {

    companion object {
        const val REQUEST_TAKE_PHOTO = 1
    }

    private val alertBuilderUtil = AlertBuilderUtil()

    private var currentPhotoPath : File? = null

    fun dispatchTakePictureIntent(activity: Activity) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {

            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                alertBuilderUtil.alertBuilder(R.string.erro_foto, AlertBuilderUtil.Type.ERROR, activity)
            }


            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(activity, "com.example.feiradasprofissoes", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                activity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {

        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "com.example.feiradasprofissoes"
        )

        mediaStorageDir.apply {
            if (!exists()) {
                if (!mkdirs()) {
                    Log.d("Feira das Profissões", "failed to create directory")
                    return null
                }
            }
        }

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//        val imageFileName = "PNG_" + timeStamp + "_"
        val image = File("${mediaStorageDir.path}${File.separator}IMG_$timeStamp.jpg")

        currentPhotoPath  = image
        return image
    }

    fun getPhotoUri(context: Context): Uri {
        return FileProvider.getUriForFile(context, "com.example.feiradasprofissoes", currentPhotoPath!!)
    }

}