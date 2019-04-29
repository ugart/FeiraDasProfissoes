package com.example.feiradasprofissoes.modules.curso

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.feiradasprofissoes.R
import com.example.feiradasprofissoes.modules.instagramSharing.InstagramSorteioActivity
import com.example.feiradasprofissoes.modules.login.view.LoginActivity
import com.example.feiradasprofissoes.modules.util.ConnectionUtils
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var id: String? = ""
    private var toolbarTitleCurso: String? = "-"
    private var titleCurso: String? = "-"
    private var textCurso: String? = "-"
    private var linkCurso: String? = "-"

    private var doubleBackToExitPressedOnce = false
    private val mHandler = Handler()
    private val mRunnable = Runnable { doubleBackToExitPressedOnce = false }

    private var mAuth: FirebaseAuth? = null

    private var mDatabase: DatabaseReference? = null

    private var checkBoxChecked: Boolean? = false

    val listener = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {}

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            checkBoxChecked =
                    dataSnapshot.child("user").child(mAuth?.currentUser!!.uid).child("checkBoxChecked")
                            .getValue(Boolean::class.java)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = "Cursos de Tecnologia da UNIFOR"

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        if (mAuth?.currentUser != null) {
            mDatabase?.addValueEventListener(listener)
        }

        val justifyTag = "<html><body style='text-align:justify;'>%s</body></html>"
        val dataString = String.format(Locale.getDefault(), justifyTag, "*Clique nos cartões acima para obter mais informações dos cursos")
        labelCursos.loadDataWithBaseURL("", dataString, "text/html", "UTF-8", "")

        sairButton.setOnClickListener { botaoLogoutClicked() }

        cardCC.setOnClickListener { cardCcClicked() }

        cardEC.setOnClickListener { cardEcClicked() }

        cardECA.setOnClickListener { cardEcaClicked() }

        cardADS.setOnClickListener { cardAdsClicked() }

        irSorteio.setOnClickListener {
            startActivity(Intent(applicationContext, InstagramSorteioActivity::class.java))
        }

    }

    private fun cardCcClicked() {
        val intent = Intent(this, DetalheCursoActivity::class.java)

        id = "1"
        toolbarTitleCurso = "Sobre Ciências da Computação"
        titleCurso = "Ciências da Computação"
        textCurso = "Esse curso é para quem quer trabalhar com projeto e desenvolvimento de software, incluindo aplicações para dispositivos móveis e jogos eletrônicos e planeja " +
                "atuar na elaboração autoral de produções gráficas, manutenção do ambiente tecnológico das organizações, gestão de projetos e governança de Tecnologia da Informação (TI)."
        linkCurso = "https://www.unifor.br/web/graduacao/ciencia-da-computacao"

        intent.putExtra("ID", id)
        intent.putExtra("TITLE_TOOLBAR_CURSO", toolbarTitleCurso)
        intent.putExtra("TITLE_CURSO", titleCurso)
        intent.putExtra("TEXT_CURSO", textCurso)
        intent.putExtra("LINK_CURSO", linkCurso)
        startActivity(intent)
    }

    private fun cardEcClicked() {
        val intent = Intent(this, DetalheCursoActivity::class.java)

        id = "2"
        toolbarTitleCurso = "Sobre Engenharia de Computação"
        titleCurso = "Engenharia de Computação"
        textCurso = "Esse curso é para quem quer trabalhar com soluções de hardware e software que envolvem as áreas de eletrônica, computação, telecomunicações e automação, e deseja " +
                "atuar no desenvolvimento de sistemas de computação e sistemas embarcados e em projetos e execuções de manutenção de equipamentos e dispositivos de computação e comunicação de dados."
        linkCurso = "https://www.unifor.br/web/graduacao/engenharia-da-computacao"

        intent.putExtra("ID", id)
        intent.putExtra("TITLE_TOOLBAR_CURSO", toolbarTitleCurso)
        intent.putExtra("TITLE_CURSO", titleCurso)
        intent.putExtra("TEXT_CURSO", textCurso)
        intent.putExtra("LINK_CURSO", linkCurso)
        startActivity(intent)
    }

    private fun cardEcaClicked() {
        val intent = Intent(this, DetalheCursoActivity::class.java)

        id = "3"
        toolbarTitleCurso = "Sobre Engenharia de Controle e Automação"
        titleCurso = "Engenharia de Controle e Automação"
        textCurso = "Esse curso é para quem quer projetar, operar e fazer a manutenção de equipamentos utilizados nos processos automatizados de indústrias em geral e deseja " +
                "ser capaz de atuar em diversas atividades, como setores industriais, comerciais e de serviços e trabalhar com gerenciamento de projetos de automação, programação de máquinas e adaptação de softwares aos processos industriais"
        linkCurso = "https://www.unifor.br/web/graduacao/engenharia-de-controle-e-automacao"

        intent.putExtra("ID", id)
        intent.putExtra("TITLE_TOOLBAR_CURSO", toolbarTitleCurso)
        intent.putExtra("TITLE_CURSO", titleCurso)
        intent.putExtra("TEXT_CURSO", textCurso)
        intent.putExtra("LINK_CURSO", linkCurso)
        startActivity(intent)
    }

    private fun cardAdsClicked() {
        val intent = Intent(this, DetalheCursoActivity::class.java)

        id = "4"
        toolbarTitleCurso = "Sobre Análise e Desenvolvimento de Sistema"
        titleCurso = "Análise e Desenvolvimento de Sistema"
        textCurso = "Esse curso é para quem quer desenvolver, analisar, projetar, especificar, implementar e atualizar sistemas computacionais de informação e planeja " +
                "estar apto a implementar soluções científicas, tecnológicas e contribuir para o desenvolvimento da área da computação e a sua aplicação no âmbito das organizações públicas e privadas da sociedade."
        linkCurso = "https://www.unifor.br/web/graduacao/analise-e-desenvolvimento-de-sistema"

        intent.putExtra("ID", id)
        intent.putExtra("TITLE_TOOLBAR_CURSO", toolbarTitleCurso)
        intent.putExtra("TITLE_CURSO", titleCurso)
        intent.putExtra("TEXT_CURSO", textCurso)
        intent.putExtra("LINK_CURSO", linkCurso)
        startActivity(intent)
    }

    //--------------------------------------- FUNÇÕES DE LOGOUT INÍCIO ------------------------------------------\\

    private fun botaoLogoutClicked() {
        if (ConnectionUtils.isConnectedToInternet(this)) {
            userLoggedOut(false)
            mAuth?.signOut()
            userLoggedOutChangeActivity()
        } else {
            val snack = Snackbar.make(constraintLayoutMainActivity, "Você está sem conexão com a internet!", Snackbar.LENGTH_SHORT)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snack.show()
        }
    }

    private fun userLoggedOut(isUserLoggedIn: Boolean) {
        mDatabase?.child("user")?.child(mAuth?.currentUser!!.uid)?.child("userLoggedIn")?.setValue(isUserLoggedIn)

        if (checkBoxChecked == true) {
            mDatabase?.child("user")?.child(mAuth?.currentUser!!.uid)?.child("checkBoxChecked")?.setValue(false)
        }
    }

    private fun userLoggedOutChangeActivity() {
        if (mAuth?.currentUser == null) {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
    }

    override fun finish() {
        super.finish()
        mDatabase?.removeEventListener(listener)
    }

    //--------------------------------------- FUNÇÕES DE LOGOUT FIM ------------------------------------------\\

    override fun onDestroy() {
        super.onDestroy()

        mHandler.removeCallbacks(mRunnable)
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true

        val snack = Snackbar.make(constraintLayoutMainActivity, "Clique novamente para sair do aplicativo", Snackbar.LENGTH_SHORT)
        snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.mediumGray))
        snack.show()

        mHandler.postDelayed(mRunnable, 2000)
    }

}


