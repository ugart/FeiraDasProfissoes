package com.example.feiradasprofissoes.modules.login.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.feiradasprofissoes.R
import com.example.feiradasprofissoes.modules.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    //TODO: Fazer verificação de internet ao tentar realizar o login

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            validations()
        }

        irCadastroText.setOnClickListener {
            startActivity(Intent(this@LoginActivity, CadastroActivity::class.java))
        }

    }

    private fun validations() {
        val email = campoEmail.text.toString().trim()
        val senha = campoSenha.text.toString().trim()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this@LoginActivity, "Por favor, digite seu e-mail", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this@LoginActivity, "Por favor, digite um e-mail válido", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(senha)) {
            Toast.makeText(this@LoginActivity, "Por favor, digite sua senha", Toast.LENGTH_SHORT).show()
            return
        }

        if (senha.length < 8) {
            Toast.makeText(this@LoginActivity,
                    "Ops...! Você deve ter digitado algo errado. A senha contém, no mínimo, 8 caracteres. Confira com cuidado!", Toast.LENGTH_SHORT).show()
            return
        }

        mAuth!!.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this@LoginActivity) { task ->
                    if (task.isSuccessful) {

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()

                    } else {
                        Toast.makeText(this@LoginActivity, "Falha ao tentar realizar o login. Confira seus dados!", Toast.LENGTH_SHORT).show()
                    }

                }
    }

}
