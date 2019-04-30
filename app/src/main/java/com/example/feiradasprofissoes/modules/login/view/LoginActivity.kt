package com.example.feiradasprofissoes.modules.login.view

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.util.Patterns
import com.example.feiradasprofissoes.R
import com.example.feiradasprofissoes.modules.UserData
import com.example.feiradasprofissoes.modules.curso.MainActivity
import com.example.feiradasprofissoes.modules.util.ConnectionUtils
import com.example.feiradasprofissoes.modules.util.hideKeyboard
import com.example.feiradasprofissoes.modules.util.setGone
import com.example.feiradasprofissoes.modules.util.setVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var mDatabase: DatabaseReference? = null

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        loginButton.setOnClickListener {
            validations()
        }

        irCadastroText.setOnClickListener {
            startActivity(Intent(this@LoginActivity, CadastroActivity::class.java))
        }

        if(!ConnectionUtils.isConnectedToInternet(this)) {
            val snack = Snackbar.make(constraintLayoutLogin, "Você está sem conexão com a internet!", Snackbar.LENGTH_SHORT)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snack.show()
        }

    }

    private fun validations() {

        constraintLayoutLogin.hideKeyboard()

        val email = campoEmail.text.toString().trim()
        val senha = campoSenha.text.toString().trim()

        if (TextUtils.isEmpty(email)) {
            campoEmail.error = "Por favor, digite seu e-mail"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            campoEmail.error = "Por favor, digite um e-mail válido"
            return
        }

        if (TextUtils.isEmpty(senha)) {
            val snack = Snackbar.make(constraintLayoutLogin, "Por favor, digite sua senha", Snackbar.LENGTH_SHORT)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            snack.show()
            return
        }

        if (senha.length < 8) {
            val snack = Snackbar.make(
                constraintLayoutLogin,
                "Ops...! Você deve ter digitado algo errado. A senha contém, no mínimo, 8 caracteres. Confira com cuidado!",
                Snackbar.LENGTH_LONG
            )
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            snack.show()
            return
        }

        containerLogin.setGone()
        progressBarLogin.setVisible()

        if (ConnectionUtils.isConnectedToInternet(this)) {

            mAuth!!.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this@LoginActivity) { task ->

                containerLogin.setVisible()
                progressBarLogin.setGone()

                if (mAuth?.currentUser != null) {

                    if (mAuth?.currentUser!!.isEmailVerified) {

                        if (task.isSuccessful) {

                            if (checkPemaneceLogado.isChecked) {
                                checkBoxIsChecked(true)
                            }

                            userLoggedIn(true)
                            startNewActivity()

                        } else {
                            val snack = Snackbar.make(
                                constraintLayoutLogin,
                                "Falha ao tentar realizar o login. Confira seus dados!",
                                Snackbar.LENGTH_SHORT
                            )
                            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                            snack.show()
                        }

                    } else {
                        val snack = Snackbar.make(
                            constraintLayoutLogin,
                            "Você precisa verificar seu e-mail para realizar o login no aplicativo",
                            Snackbar.LENGTH_LONG
                        )
                        snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
                        snack.show()
                    }

                } else {
                    val snack = Snackbar.make(
                        constraintLayoutLogin,
                        "Este e-mail não está cadastrado. Realize seu cadastro para utilizar o aplicativo!",
                        Snackbar.LENGTH_LONG
                    )
                    snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
                    snack.show()
                }

            }

        } else {
            containerLogin.setVisible()
            progressBarLogin.setGone()
            val snack =
                Snackbar.make(constraintLayoutLogin, "Você está sem conexão com a internet!", Snackbar.LENGTH_SHORT)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snack.show()
        }

    }

    private fun userLoggedIn(isUserLoggedIn: Boolean) {
        mDatabase?.child("user")?.child(mAuth?.currentUser!!.uid)?.child("userLoggedIn")?.setValue(isUserLoggedIn)
        mDatabase?.keepSynced(true)
    }

    private fun checkBoxIsChecked(isCheckBoxChecked: Boolean) {
        mDatabase?.child("user")?.child(mAuth?.currentUser!!.uid)?.child("checkBoxChecked")?.setValue(isCheckBoxChecked)
        mDatabase?.keepSynced(true)
    }

    private fun startNewActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}
