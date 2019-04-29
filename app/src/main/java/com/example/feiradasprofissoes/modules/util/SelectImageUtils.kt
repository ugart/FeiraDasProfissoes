package com.example.feiradasprofissoes.modules.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.feiradasprofissoes.R
import com.example.feiradasprofissoes.modules.util.AlertBuilderUtil.Type.ERROR
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.dialog_camera.*
import java.io.File

open class SelectImageUtils(val context: Context) {

    private var cameraUtil = CameraUtil()

    private var callback: SelectImageUtilCallback? = null

    private val alertBuilderUtil = AlertBuilderUtil()

    interface SelectImageUtilCallback {
        fun onImageLoaded(uri: Uri)
    }

    @SuppressLint("InlinedApi")
    fun showDialogSelectImageFrom(activity: Activity) {
        val dialog = Dialog(activity, R.style.alert)

        dialog.setContentView(R.layout.dialog_camera)
        dialog.show()

        dialog.escolher_galeria.setOnClickListener {
            val permissionMedia =
                    ContextCompat.checkSelfPermission(it.context, Manifest.permission.READ_EXTERNAL_STORAGE)
            val permissionWrite =
                    ContextCompat.checkSelfPermission(it.context, Manifest.permission.WRITE_EXTERNAL_STORAGE)

            if (permissionMedia == PackageManager.PERMISSION_GRANTED && permissionWrite == PackageManager.PERMISSION_GRANTED) {
                abrirGaleria(activity)
                dialog.dismiss()
            } else {
                ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0
                )
            }
        }

        dialog.escolher_camera.setOnClickListener {
            val permissionCamera = ContextCompat.checkSelfPermission(it.context, Manifest.permission.CAMERA)
            val permissionWrite =
                    ContextCompat.checkSelfPermission(it.context, Manifest.permission.WRITE_EXTERNAL_STORAGE)

            if (permissionCamera == PackageManager.PERMISSION_GRANTED && permissionWrite == PackageManager.PERMISSION_GRANTED) {
                abrirCamera(activity)
                dialog.dismiss()
            } else {
                ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0
                )
            }
        }

    }

    fun handlerActivityResult(requestCode: Int, resultCode: Int, data: Intent?, activity: Activity) {
        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_PHOTO_GALLERY) {
                if (data != null) {
                    startCropActivity(data.data!!, activity)
                } else {
                    alertBuilderUtil.alertBuilder(R.string.erro_selecionar_foto, ERROR, activity)
                }
            } else if (requestCode == CameraUtil.REQUEST_TAKE_PHOTO) {
                startCropActivity(cameraUtil.getPhotoUri(context), activity)
            } else if (requestCode == UCrop.REQUEST_CROP) {
                if (callback != null && data != null) {
                    UCrop.getOutput(data)?.let {
                        callback!!.onImageLoaded(it)
                    }
                }
            }

        }
    }

    fun setSelectImageUtilCallback(callback: SelectImageUtilCallback) {
        this.callback = callback
    }

    private fun abrirGaleria(activity: Activity) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        activity.startActivityForResult(intent, REQUEST_PHOTO_GALLERY)
    }

    private fun abrirCamera(activity: Activity) {
        cameraUtil.dispatchTakePictureIntent(activity)
    }

    private fun startCropActivity(uri: Uri, activity: Activity) {

        val destinationFileName = System.currentTimeMillis().toString() + ".jpg"

        var uCrop = UCrop.of(uri, Uri.fromFile(File(activity.cacheDir, destinationFileName)))

        uCrop = configUCrop(uCrop, activity)

        uCrop.start(activity)

    }

    private fun configUCrop(uCrop: UCrop, context: Context): UCrop {
        val options = UCrop.Options()

        options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
        options.setCompressionQuality(100)
        options.setHideBottomControls(false)
        options.setFreeStyleCropEnabled(true)
        options.setToolbarTitle(context.getString(R.string.editar_foto))
        options.setToolbarColor(ContextCompat.getColor(context, R.color.white))
        options.setStatusBarColor(ContextCompat.getColor(context, R.color.white))
        options.useSourceImageAspectRatio()
        options.withMaxResultSize(640, 640)
        options.withAspectRatio(1f, 1f)

        return uCrop.withOptions(options)
    }

    companion object {

        private const val REQUEST_PHOTO_GALLERY = 2
    }

}
