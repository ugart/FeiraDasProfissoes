package com.example.feiradasprofissoes.modules.login.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
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

        //fazer o teclado sumir

        val email = campoEmail.text.toString().trim()
        val senha = campoSenha.text.toString().trim()

        if (TextUtils.isEmpty(email)) {
            val snack = Snackbar.make(constraintLayoutLogin, "Por favor, digite seu e-mail", Snackbar.LENGTH_SHORT)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            snack.show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            val snack = Snackbar.make(constraintLayoutLogin, "Por favor, digite um e-mail válido", Snackbar.LENGTH_SHORT)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            snack.show()
            return
        }

        if (TextUtils.isEmpty(senha)) {
            val snack = Snackbar.make(constraintLayoutLogin, "Por favor, digite sua senha", Snackbar.LENGTH_SHORT)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            snack.show()
            return
        }

        if (senha.length < 8) {
            val snack = Snackbar.make(constraintLayoutLogin, "Ops...! Você deve ter digitado algo errado. A senha contém, no mínimo, 8 caracteres. Confira com cuidado!",
                    Snackbar.LENGTH_SHORT)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            snack.show()
            return
        }

        if (isNetworkAvailable()) {
//TODO: verificar pq ele continua dizendo que precisa verificar o e-mail mesmo depois de estar com o e-mail verificado (E faz o login mesmo com esse aviso)


            mAuth!!.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this@LoginActivity) { task ->

                if (mAuth?.currentUser!!.isEmailVerified) {

                    if (task.isSuccessful) {

                        startNewActivity()

                    } else {
                        val snack = Snackbar.make(constraintLayoutLogin, "Falha ao tentar realizar o login. Confira seus dados!", Snackbar.LENGTH_SHORT)
                        snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                        snack.show()
                    }

                } else {
                    val snack = Snackbar.make(constraintLayoutLogin, "Você precisa verificar seu e-mail para realizar o login no aplicativo", Snackbar.LENGTH_SHORT)
                    snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
                    snack.show()
                }

            }

        } else {
            val snack = Snackbar.make(constraintLayoutLogin, "Você está sem conexão com a internet!", Snackbar.LENGTH_SHORT)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snack.show()
        }

    }

    private fun startNewActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

}
