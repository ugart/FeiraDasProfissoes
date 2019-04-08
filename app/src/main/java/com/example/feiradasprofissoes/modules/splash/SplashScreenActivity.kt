package com.example.feiradasprofissoes.modules.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.example.feiradasprofissoes.R
import com.example.feiradasprofissoes.modules.login.CadastroActivity

class SplashScreenActivity : AppCompatActivity() {
    //TODO: tratar splash pra quando receber notificações levar até a tela específica
    //TODO: fazer verificação se o usuário está logado

    private fun exitSplashScreen(runnable: Runnable) {
        Handler().postDelayed(runnable, 3000)//Inicializando o handler e definindo delay de 3 segundos pra tela de splash
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        exitSplashScreen(Runnable {
            startActivity(Intent(applicationContext, CadastroActivity::class.java))
            finish()
        })

    }

}