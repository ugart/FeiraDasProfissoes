package com.example.feiradasprofissoes.modules

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.feiradasprofissoes.R
import com.example.feiradasprofissoes.modules.login.view.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        sairButton.setOnClickListener {
            mAuth?.signOut()

            userLoggedOutChangeActivity()

        }

        userLoggedOutChangeActivity()
    }

    private fun userLoggedOutChangeActivity() {
        if (mAuth?.currentUser == null) {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }
    }
}

