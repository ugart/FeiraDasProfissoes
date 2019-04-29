package com.example.feiradasprofissoes.modules.instagramSharing

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.feiradasprofissoes.R
import com.example.feiradasprofissoes.modules.util.CameraUtil
import com.example.feiradasprofissoes.modules.util.ConnectionUtils
import com.example.feiradasprofissoes.modules.util.SelectImageUtils
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_instagram.*
import java.io.File
import java.io.FileOutputStream
import java.util.*

class InstagramSorteioActivity : AppCompatActivity() {

    var type = "image/jpg"
    var filename = "/myPhoto.jpg"
    var mediaPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + filename

    private var cameraUtil: CameraUtil? = null

    lateinit var uri: Uri

    private var selectImageUtils: SelectImageUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instagram)

        selectImageUtils = SelectImageUtils(this)
        cameraUtil = CameraUtil()

        setSupportActionBar(toolbarInstaSorteio)

        if (supportActionBar != null) {
            supportActionBar!!.title = "Sorteio no Instagram"
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        val justifyTag = "<html><body style='text-align:justify;'>%s</body></html>"
        val dataString = String.format(Locale.getDefault(), justifyTag, "Clique na imagem acima para trocar sua foto e no botão abaixo para compartilhá-la no Instagram. " +
                "Tire uma foto bem legal, compartilhe a foto acima no Instagram com a hashtag <b> #feiradasprofissoesunifor </b> e participe do sorteio!")
        label.loadDataWithBaseURL("", dataString, "text/html", "UTF-8", "")

        clipboardCopyHashtagButton.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("", "#feiradasprofissoesunifor")
            clipboard.primaryClip = clip

            val snack = Snackbar.make(constraintLayoutInstagramSorteio, "Hashtag copiada!", Snackbar.LENGTH_SHORT)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            snack.show()
        }

        toolbarInstaSorteio.setNavigationOnClickListener { onBackPressed() }

        compartilharInstagram.setOnClickListener {

            if (ConnectionUtils.isConnectedToInternet(this)) {

                createInstagramIntent(uri)

            } else {
                val snack = Snackbar.make(
                        constraintLayoutInstagramSorteio,
                        "Você está sem conexão com a internet!",
                        Snackbar.LENGTH_SHORT
                )
                snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                snack.show()
            }

        }

        selectImageUtils?.setSelectImageUtilCallback(object : SelectImageUtils.SelectImageUtilCallback {
            override fun onImageLoaded(uri: Uri) {
                this@InstagramSorteioActivity.uri = uri
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

    private fun createInstagramIntent(uri: Uri) {

        var intent = packageManager.getLaunchIntentForPackage("com.instagram.android")

        if (intent != null) {

            val filePath = uri.path
            val bitmap = BitmapFactory.decodeFile(filePath)
            var path = mediaPath
            val file = File(path)

            try {
                var out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            path = file.path
            val bmpUri = Uri.parse("file://$path")

            var shareIntent = Intent(android.content.Intent.ACTION_SEND)
            shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            shareIntent.setPackage("com.instagram.android")
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
//        shareIntent.putExtra(Intent.EXTRA_TEXT, "#feiradasprofissoesunifor")
            shareIntent.type = type
            startActivity(Intent.createChooser(shareIntent, "Compartilhe sua foto no feed do instagram"))

        } else {
            intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.parse("market://details?id=" + "com.instagram.android")
            startActivity(intent)
        }

    }

    private fun loadImage(photoUri: String?) {

        try {
            if (photoUri != null) {
                Picasso.with(this)
                        .load(photoUri)
                        .placeholder(R.drawable.placeholder_img_sorteio)
                        .into(fotoInstaEvento)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}