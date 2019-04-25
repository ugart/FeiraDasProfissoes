package com.example.feiradasprofissoes.modules.instagramSharing

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.feiradasprofissoes.R
import com.example.feiradasprofissoes.modules.util.ConnectionUtils
import com.example.feiradasprofissoes.modules.util.SelectImageUtils
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_instagram.*
import java.io.File

class InstagramSorteioActivity : AppCompatActivity() {

    private var selectImageUtils: SelectImageUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instagram)

        selectImageUtils = SelectImageUtils(this)

        setSupportActionBar(toolbarInstaSorteio)

        if (supportActionBar != null) {
            supportActionBar!!.title = "Sorteio no Instagram"
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        toolbarInstaSorteio.setNavigationOnClickListener { onBackPressed() }

        compartilharInstagram.setOnClickListener {

            if (ConnectionUtils.isConnectedToInternet(this)) {
                instaSharing()
            } else {
                val snack = Snackbar.make(constraintLayoutInstagramSorteio, "Você está sem conexão com a internet!", Snackbar.LENGTH_SHORT)
                snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                snack.show()
            }

        }

//        fotoInstaEvento.invalidate()

        selectImageUtils?.setSelectImageUtilCallback(object : SelectImageUtils.SelectImageUtilCallback {
            override fun onImageLoaded(uri: Uri) {
                loadImage(uri.toString())
            }
        })

        fotoInstaEvento.setOnClickListener {
            selectImageUtils?.showDialogSelectImageFrom(this@InstagramSorteioActivity)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        selectImageUtils?.handlerActivityResult(requestCode, resultCode, data, this)
    }

    fun instaSharing() {

        val dir = File(Environment.getExternalStorageDirectory(), "FolderName")
        val imgFile = File(dir, "0.png")
        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.type = "image/*"
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://$imgFile"))
        sendIntent.putExtra(Intent.EXTRA_TEXT, "#feiradasprofissoesunifor")
        sendIntent.setPackage("com.instagram.android")
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        try {
            startActivity(Intent.createChooser(sendIntent, "Share images..."))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(this, "Por favor, baixe o aplicativo do Instagram neste dispositivo!", Toast.LENGTH_LONG).show()
        }

    }

    private fun loadImage(photoUri: String?) {

        try {
            if (photoUri != null) {
                Picasso.with(this)
                        .load(photoUri)
                        .placeholder(R.drawable.ecaimage)
                        .transform(CropCircleTransformation()).into(fotoInstaEvento)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}