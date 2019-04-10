package com.example.feiradasprofissoes.modules.login.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Patterns
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import com.example.feiradasprofissoes.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cadastro.*


//TODO: Enviar todas as informações de cadastro ao firebase
//TODO: Ajustar codigo para a arquitetura MVVM e criar um util para trocar toasts por snackbars
//TODO: Fazer verificação de internet ao tentar realizar o cadastro

class CadastroActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        mAuth = FirebaseAuth.getInstance()

        cadastroButton.setOnClickListener {
            validations()
        }

    }

    private fun validations() {

        //fazer o teclado sumir

        val nome = campoNomeCadastro.text.toString().trim()
        val email = campoEmailCadastro.text.toString().trim()
        val senha = campoSenhaCadastro.text.toString().trim()
        val confirmaSenha = campoConfirmaSenhaCadastro.text.toString().trim()
        val nomeCidade = campoNomeCidadeCadastro.text.toString().trim()
        val nomeEscola = campoNomeEscolaCadastro.text.toString().trim()

        if (TextUtils.isEmpty(nome)) {
            val snack = Snackbar.make(constraintLayoutCadastro, "Por favor, digite seu nome", Snackbar.LENGTH_SHORT)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            snack.show()
            return
        }

        if (TextUtils.isEmpty(email)) {
            val snack = Snackbar.make(constraintLayoutCadastro, "Por favor, digite seu e-mail", Snackbar.LENGTH_SHORT)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            snack.show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            val snack = Snackbar.make(constraintLayoutCadastro, "Por favor, digite um e-mail válido", Snackbar.LENGTH_SHORT)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            snack.show()
            return
        }

        if (TextUtils.isEmpty(senha)) {
            val snack = Snackbar.make(constraintLayoutCadastro, "Por favor, digite sua senha", Snackbar.LENGTH_SHORT)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            snack.show()
            return
        }

        if (senha.length < 8) {
            val snack = Snackbar.make(constraintLayoutCadastro, "Você precisa de, no mínimo, 8 (oito) caracteres para compor sua senha", Snackbar.LENGTH_SHORT)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            snack.show()
            return
        }

        if (TextUtils.isEmpty(confirmaSenha)) {
            val snack = Snackbar.make(constraintLayoutCadastro, "Por favor, confirme sua senha", Snackbar.LENGTH_SHORT)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            snack.show()
            return
        }

        if (senha != confirmaSenha) {
            val snack = Snackbar.make(constraintLayoutCadastro, "As senhas digitadas não conferem", Snackbar.LENGTH_SHORT)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            snack.show()
            return
        }

        if (TextUtils.isEmpty(nomeEscola)) {
            val snack = Snackbar.make(constraintLayoutCadastro, "Por favor, digite o nome da escola onde você estuda", Snackbar.LENGTH_SHORT)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            snack.show()
            return
        }

        if (TextUtils.isEmpty(nomeCidade)) {
            val snack = Snackbar.make(constraintLayoutCadastro, "Por favor, digite o nome da cidade onde você mora", Snackbar.LENGTH_SHORT)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            snack.show()
            return
        }

        switchScreenItensVisibility()
        progressBar.visibility = VISIBLE

        if (senha == confirmaSenha) {

            mAuth?.fetchSignInMethodsForEmail(email)?.addOnCompleteListener { task ->
                if (task.result.signInMethods!!.size == 0) {

                    if (isNetworkAvailable()) {

                        mAuth!!.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(this@CadastroActivity) {

                            switchScreenItensVisibility()
                            progressBar.visibility = GONE

                            if (task.isSuccessful) {

                                verifyEmail()

                            } else {
                                val snack = Snackbar.make(constraintLayoutCadastro, "Seu cadastro falhou!", Snackbar.LENGTH_SHORT)
                                snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                                snack.show()
                            }

                        }

                    } else {
                        val snack = Snackbar.make(constraintLayoutCadastro, "Você está sem conexão com a internet!", Snackbar.LENGTH_SHORT)
                        snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                        snack.show()
                    }

                } else {
                    val snack = Snackbar.make(constraintLayoutCadastro, "Já existe uma conta criada com o e-mail informado!", Snackbar.LENGTH_SHORT)
                    snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                    snack.show()
                }

            }?.addOnFailureListener { e -> e.printStackTrace() }

        }

    }

    private fun verifyEmail() {
        val user = mAuth?.currentUser

        user?.sendEmailVerification()?.addOnCompleteListener(this@CadastroActivity) { task ->

            if (task.isSuccessful) {

                if (task.isSuccessful) {
                    val builder = AlertDialog.Builder(this@CadastroActivity)

                    builder.setMessage("Seu e-mail de verificação foi enviado para  " + user.email + "! O e-mail pode levar algum tempinho para chegar. Ah, e não esqueça de conferir na caixa de SPAM, ok?")

                    builder.setPositiveButton("OK") { _, _ ->
                        startNewActivity()
                    }

                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                } else {
                    val snack = Snackbar.make(constraintLayoutCadastro, "Desculpe, não conseguimos enviar seu e-mail de verificação", Snackbar.LENGTH_SHORT)
                    snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                    snack.show()
                }

            }

        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    private fun startNewActivity() {
        val intent = Intent(this@CadastroActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun switchScreenItensVisibility() {

        if (campoNomeCadastroLayout.visibility == VISIBLE) {
            campoNomeCadastroLayout.visibility = GONE
        } else {
            campoNomeCadastroLayout.visibility = VISIBLE
        }

        if (campoEmailCadastroLayout.visibility == VISIBLE) {
            campoEmailCadastroLayout.visibility = GONE
        } else {
            campoEmailCadastroLayout.visibility = VISIBLE
        }

        if (senhaCadastroLayout.visibility == VISIBLE) {
            senhaCadastroLayout.visibility = GONE
        } else {
            senhaCadastroLayout.visibility = VISIBLE
        }

        if (confirmaSenhaCadastroLayout.visibility == VISIBLE) {
            confirmaSenhaCadastroLayout.visibility = GONE
        } else {
            confirmaSenhaCadastroLayout.visibility = VISIBLE
        }

        if (campoNomeEscolaCadastroLayout.visibility == VISIBLE) {
            campoNomeEscolaCadastroLayout.visibility = GONE
        } else {
            campoNomeEscolaCadastroLayout.visibility = VISIBLE
        }

        if (campoNomeCidadeCadastroLayout.visibility == VISIBLE) {
            campoNomeCidadeCadastroLayout.visibility = GONE
        } else {
            campoNomeCidadeCadastroLayout.visibility = VISIBLE
        }

        if (labelTipoEscola.visibility == VISIBLE) {
            labelTipoEscola.visibility = GONE
        } else {
            labelTipoEscola.visibility = VISIBLE
        }

        if (tipoEscolaCadastro.visibility == VISIBLE) {
            tipoEscolaCadastro.visibility = GONE
        } else {
            tipoEscolaCadastro.visibility = VISIBLE
        }

    }

}