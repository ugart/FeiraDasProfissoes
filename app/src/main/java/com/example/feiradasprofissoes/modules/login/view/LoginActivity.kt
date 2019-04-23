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
import com.example.feiradasprofissoes.R
import com.example.feiradasprofissoes.modules.MainActivity
import com.example.feiradasprofissoes.modules.util.hideKeyboard
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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
                Snackbar.LENGTH_SHORT
            )
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            snack.show()
            return
        }

        if (isNetworkAvailable()) {

            mAuth!!.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this@LoginActivity) { task ->

                if (mAuth?.currentUser != null) {

                    if (mAuth?.currentUser!!.isEmailVerified) {

                        if (task.isSuccessful) {

                            if (checkPemaneceLogado.isChecked) {
                                checkBoxIsChecked(true)
//                                checkBoxIsMarked(true)
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
                            Snackbar.LENGTH_SHORT
                        )
                        snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
                        snack.show()
                    }

                } else {
                    val snack = Snackbar.make(
                        constraintLayoutLogin,
                        "Este e-mail não está cadastrado. Realize seu cadastro para utilizar o aplicativo!",
                        Snackbar.LENGTH_SHORT
                    )
                    snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
                    snack.show()
                }

            }

        } else {
            val snack =
                Snackbar.make(constraintLayoutLogin, "Você está sem conexão com a internet!", Snackbar.LENGTH_SHORT)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snack.show()
        }

    }

    private fun userLoggedIn(isUserLoggedIn: Boolean) {
        mDatabase?.child("user")?.child(mAuth?.currentUser!!.uid)?.child("userLoggedIn")?.setValue(isUserLoggedIn)
    }

    private fun checkBoxIsChecked(isCheckBoxChecked: Boolean) {
        mDatabase?.child("user")?.child(mAuth?.currentUser!!.uid)?.child("checkBoxChecked")?.setValue(isCheckBoxChecked)
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
