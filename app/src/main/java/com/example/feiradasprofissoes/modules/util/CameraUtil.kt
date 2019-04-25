package com.example.feiradasprofissoes.modules.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
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
                photoFile = createImageFile(activity)
            } catch (ex: IOException) {
                alertBuilderUtil.alertBuilder(R.string.erro_foto, AlertBuilderUtil.Type.ERROR, activity)
            }


            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(activity,
                        "com.example.feiradasprofissoes",
                        photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                activity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(context: Context): File {

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        )

        currentPhotoPath  = image
        return image
    }

    fun getPhotoUri(context: Context): Uri {
        return FileProvider.getUriForFile(context, "com.example.feiradasprofissoes", currentPhotoPath !!)
    }

}