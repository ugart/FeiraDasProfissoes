package com.example.feiradasprofissoes.modules.curso

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.example.feiradasprofissoes.R
import kotlinx.android.synthetic.main.activity_detalhe_curso.*

class DetalheCursoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe_curso)

        val intent = intent

        val id = intent.getStringExtra("ID")
        val toolbarTitleCurso = intent.getStringExtra("TITLE_TOOLBAR_CURSO")
        val titleCurso = intent.getStringExtra("TITLE_CURSO")
        val textCurso = intent.getStringExtra("TEXT_CURSO")
        val linkCurso = intent.getStringExtra("LINK_CURSO")

        nomeCurso.text = titleCurso
        descricaoCurso.text = textCurso
        webviewCurso.text = getString(R.string.maisInfoCurso, linkCurso)

        //TODO: abrir webview (se tiver com internet) ao clicar no link

        setSupportActionBar(toolbar)

        if (supportActionBar != null) {
            supportActionBar!!.title = toolbarTitleCurso
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        toolbar.setNavigationOnClickListener { onBackPressed() }

        when (id) {
            1.toString() -> imageCurso.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ccimage))
            2.toString() -> imageCurso.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ecimage))
            3.toString() -> imageCurso.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ecaimage))
            4.toString() -> imageCurso.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.adsimage))
        }

    }

}