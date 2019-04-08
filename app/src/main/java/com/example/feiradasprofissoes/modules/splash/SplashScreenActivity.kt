package com.example.feiradasprofissoes.modules.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.example.feiradasprofissoes.R
import com.example.feiradasprofissoes.modules.MainActivity
import com.example.feiradasprofissoes.modules.login.view.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {

    //TODO: tratar splash pra quando receber notificações levar até a tela específica
    //TODO: fazer verificação se o usuário está logado para encaminhar para a tela inicial (se tiver) ou para o login (se não tiver)

    private fun exitSplashScreen(runnable: Runnable) {
        Handler().postDelayed(runnable, 3000)//Inicializando o handler e definindo delay de 3 segundos pra tela de splash
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        exitSplashScreen(Runnable {

            if (FirebaseAuth.getInstance().currentUser != null) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            } else {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }

            finish()
        })

    }

}