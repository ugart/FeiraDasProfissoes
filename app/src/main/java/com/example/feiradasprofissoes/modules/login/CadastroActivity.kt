package com.example.feiradasprofissoes.modules.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.example.feiradasprofissoes.R
import com.example.feiradasprofissoes.modules.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cadastro.*

//TODO: Ajustar codigo para a arquitetura MVVM e criar um util para trocar toasts por snackbars
class CadastroActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        mAuth = FirebaseAuth.getInstance()

        cadastroButton.setOnClickListener {

            checks()

        }

    }

    private fun checks() {

        val nome = campoNomeCadastro.text.toString().trim()
        val email = campoEmailCadastro.text.toString().trim()
        val senha = campoSenhaCadastro.text.toString().trim()
        val confirmaSenha = campoConfirmaSenhaCadastro.text.toString().trim()
        val nomeCidade = campoNomeCidadeCadastro.text.toString().trim()
        val nomeEscola = campoNomeEscolaCadastro.text.toString().trim()

        if (TextUtils.isEmpty(nome)) {
            Toast.makeText(this@CadastroActivity, "Por favor, digite seu nome", Toast.LENGTH_SHORT).show()
            return
        }

        //TODO: checar se está no formato correto de email
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this@CadastroActivity, "Por favor, digite seu e-mail", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(senha)) {
            Toast.makeText(this@CadastroActivity, "Por favor, digite sua senha", Toast.LENGTH_SHORT).show()
            return
        }

        if (senha.length < 6) {
            Toast.makeText(this@CadastroActivity, "Você precisa de, no mínimo, 6 caracteres para compor sua senha", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(confirmaSenha)) {
            Toast.makeText(this@CadastroActivity, "Por favor, confirme sua senha", Toast.LENGTH_SHORT).show()
            return
        }

        if (senha != confirmaSenha) {
            Toast.makeText(this@CadastroActivity, "As senhas digitadas não conferem", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(nomeEscola)) {
            Toast.makeText(this@CadastroActivity, "Por favor, digite o nome da escola onde você estuda", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(nomeCidade)) {
            Toast.makeText(this@CadastroActivity, "Por favor, digite o nome da cidade onde você mora", Toast.LENGTH_SHORT).show()
            return
        }

        switchScreenItensVisibility()
        progressBar.visibility = VISIBLE

        if (senha == confirmaSenha) {

            mAuth!!.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this@CadastroActivity) { task ->

                    switchScreenItensVisibility()
                    progressBar.visibility = GONE

                    if (task.isSuccessful) {

                        startActivity(Intent(this@CadastroActivity, MainActivity::class.java))

                    } else {
                        Toast.makeText(this@CadastroActivity, "Seu cadastro falhou!", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    fun switchScreenItensVisibility() {

        if(campoNomeCadastro.visibility == VISIBLE) {
            campoNomeCadastro.visibility = GONE
        } else {
            campoNomeCadastro.visibility = VISIBLE
        }

        if(campoEmailCadastro.visibility == VISIBLE) {
            campoEmailCadastro.visibility = GONE
        } else {
            campoEmailCadastro.visibility = VISIBLE
        }

        if(senhaCadastroLayout.visibility == VISIBLE) {
            senhaCadastroLayout.visibility = GONE
        } else {
            senhaCadastroLayout.visibility = VISIBLE
        }

        if(confirmaSenhaCadastroLayout.visibility == VISIBLE) {
            confirmaSenhaCadastroLayout.visibility = GONE
        } else {
            confirmaSenhaCadastroLayout.visibility = VISIBLE
        }

        if(campoNomeEscolaCadastro.visibility == VISIBLE) {
            campoNomeEscolaCadastro.visibility = GONE
        } else {
            campoNomeEscolaCadastro.visibility = VISIBLE
        }

        if(tipoEscolaCadastro.visibility == VISIBLE) {
            tipoEscolaCadastro.visibility = GONE
        } else {
            tipoEscolaCadastro.visibility = VISIBLE
        }

        if(campoNomeCidadeCadastro.visibility == VISIBLE) {
            campoNomeCidadeCadastro.visibility = GONE
        } else {
            campoNomeCidadeCadastro.visibility = VISIBLE
        }

        if(labelTipoEscola.visibility == VISIBLE) {
            labelTipoEscola.visibility = GONE
        } else {
            labelTipoEscola.visibility = VISIBLE
        }

    }



}