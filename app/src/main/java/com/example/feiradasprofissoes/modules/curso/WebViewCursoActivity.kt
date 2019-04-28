package com.example.feiradasprofissoes.modules.curso

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.feiradasprofissoes.R
import kotlinx.android.synthetic.main.activity_webview_curso.*

class WebViewCursoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_curso)

        val linkCurso = intent.getStringExtra("LINK_CURSO")

        webviewCurso.loadUrl(linkCurso)
    }

}
