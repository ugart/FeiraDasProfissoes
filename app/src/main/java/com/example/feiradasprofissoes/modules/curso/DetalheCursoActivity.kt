package com.example.feiradasprofissoes.modules.curso

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import com.example.feiradasprofissoes.R
import com.example.feiradasprofissoes.modules.util.ConnectionUtils
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
        irWebviewCurso.text = getString(R.string.maisInfoCurso)

        irWebviewCurso.setOnClickListener {

            if (ConnectionUtils.isConnectedToInternet(this)) {
                val intent = Intent(this, WebViewCursoActivity::class.java)

                intent.putExtra("LINK_CURSO", linkCurso)

                startActivity(intent)
            } else {
                val snack = Snackbar.make(constraintLayoutDetalheCurso, "Você está sem conexão com a internet!", Snackbar.LENGTH_SHORT)
                snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                snack.show()
            }

        }

        setSupportActionBar(toolbarDetalheCurso)

        if (supportActionBar != null) {
            supportActionBar!!.title = toolbarTitleCurso
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        toolbarDetalheCurso.setNavigationOnClickListener { onBackPressed() }

        when (id) {
            1.toString() -> imageCurso.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ccimage))
            2.toString() -> imageCurso.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ecimage))
            3.toString() -> imageCurso.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ecaimage))
            4.toString() -> imageCurso.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.adsimage))
        }

    }

}