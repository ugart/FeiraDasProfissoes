package com.example.feiradasprofissoes.modules.login.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.feiradasprofissoes.R
import com.example.feiradasprofissoes.modules.UserData
import com.example.feiradasprofissoes.modules.util.ConnectionUtils
import com.example.feiradasprofissoes.modules.util.hideKeyboard
import com.example.feiradasprofissoes.modules.util.setGone
import com.example.feiradasprofissoes.modules.util.setVisible
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_cadastro.*

class CadastroActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var tipoEscola = arrayOf("Pública", "Privada")

    private var tipoEscolaEscolhido: String = ""

    private var mDatabase: DatabaseReference? = null

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        tipoEscolaCadastro.onItemSelectedListener = this

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipoEscola)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tipoEscolaCadastro.adapter = arrayAdapter

        cadastroButton.setOnClickListener {
            validations()
        }

        setSupportActionBar(toolbarCadastro)

        if (supportActionBar != null) {
            supportActionBar!!.title = "Cadastro"
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        toolbarCadastro.setNavigationOnClickListener { onBackPressed() }

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        tipoEscolaEscolhido = tipoEscola[position]
    }

    private fun validations() {

        constraintLayoutCadastro.hideKeyboard()

        val nome = campoNomeCadastro.text.toString().trim()
        val email = campoEmailCadastro.text.toString().trim()
        val senha = campoSenhaCadastro.text.toString().trim()
        val confirmaSenha = campoConfirmaSenhaCadastro.text.toString().trim()
        val nomeCidade = campoNomeCidadeCadastro.text.toString().trim()
        val nomeEscola = campoNomeEscolaCadastro.text.toString().trim()

        if (TextUtils.isEmpty(nome)) {
            campoNomeCadastro.error = "Por favor, digite seu nome"
            return
        }

        if (TextUtils.isEmpty(email)) {
            campoEmailCadastro.error = "Por favor, digite seu e-mail"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            campoEmailCadastro.error = "Por favor, digite um e-mail válido"
//            val snack = Snackbar.make(constraintLayoutCadastro, "Por favor, digite um e-mail válido", Snackbar.LENGTH_SHORT)
//            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
//            snack.show()
            return
        }

        if (TextUtils.isEmpty(senha)) {
            val snack = Snackbar.make(constraintLayoutCadastro, "Por favor, digite sua senha", Snackbar.LENGTH_SHORT)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            snack.show()
            return
        }

        if (senha.length < 8) {
            val snack = Snackbar.make(
                    constraintLayoutCadastro,
                    "Você precisa de, no mínimo, 8 (oito) caracteres para compor sua senha",
                    Snackbar.LENGTH_SHORT
            )
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
            val snack =
                    Snackbar.make(constraintLayoutCadastro, "As senhas digitadas não conferem", Snackbar.LENGTH_SHORT)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            snack.show()
            return
        }

        if (TextUtils.isEmpty(nomeEscola)) {
            campoNomeEscolaCadastro.error = "Por favor, digite o nome da escola onde você estuda"
            return
        }

        if (TextUtils.isEmpty(nomeCidade)) {
            campoNomeCidadeCadastro.error = "Por favor, digite o nome da cidade onde você mora"
            return
        }

        containerCadastro.setGone()
        progressBar.setVisible()

        if (senha == confirmaSenha) {

            containerCadastro.setVisible()
            progressBar.setGone()

            mAuth?.fetchSignInMethodsForEmail(email)?.addOnCompleteListener { task ->
                if (task.result.signInMethods!!.size == 0) {

                    if (ConnectionUtils.isConnectedToInternet(this)) {

                        mAuth!!.createUserWithEmailAndPassword(email, senha)
                                .addOnCompleteListener(this@CadastroActivity) {

                                    if (task.isSuccessful) {

                                        verifyEmail()
                                        writeNewUser(
                                                mAuth?.currentUser!!.uid,
                                                nome,
                                                email,
                                                nomeCidade,
                                                nomeEscola,
                                                tipoEscolaEscolhido,
                                                false,
                                                false
                                        )

                                    } else {
                                        val snack = Snackbar.make(
                                                constraintLayoutCadastro,
                                                "Seu cadastro falhou!",
                                                Snackbar.LENGTH_SHORT
                                        )
                                        snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                                        snack.show()
                                    }

                                }

                    } else {
                        val snack = Snackbar.make(
                                constraintLayoutCadastro,
                                "Você está sem conexão com a internet!",
                                Snackbar.LENGTH_SHORT
                        )
                        snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                        snack.show()
                    }

                } else {
                    val snack = Snackbar.make(
                            constraintLayoutCadastro,
                            "Já existe uma conta criada com o e-mail informado!",
                            Snackbar.LENGTH_SHORT
                    )
                    snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                    snack.show()
                }

            }?.addOnFailureListener { e -> e.printStackTrace() }

        }

    }

    private fun writeNewUser(
            userId: String,
            name: String,
            email: String,
            cityName: String,
            schoolName: String,
            schoolType: String,
            isUserLoggedIn: Boolean,
            isCheckBoxChecked: Boolean
    ) {
        val userData =
                UserData(userId, name, email, cityName, schoolName, schoolType, isUserLoggedIn, isCheckBoxChecked)
        mDatabase?.child("user")?.child(userId)?.setValue(userData)
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
                    val snack = Snackbar.make(
                            constraintLayoutCadastro,
                            "Desculpe, não conseguimos enviar seu e-mail de verificação",
                            Snackbar.LENGTH_SHORT
                    )
                    snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                    snack.show()
                }

            }

        }
    }

    private fun startNewActivity() {
        val intent = Intent(this@CadastroActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    /* private fun switchScreenItensVisibility() {

         if (campoNomeCadastroLayout.visibility == VISIBLE) {
             campoNomeCadastroLayout.setGone()
         } else {
             campoNomeCadastroLayout.setVisible()
         }

         if (campoEmailCadastroLayout.visibility == VISIBLE) {
             campoEmailCadastroLayout.setGone()
         } else {
             campoEmailCadastroLayout.setVisible()
         }

         if (senhaCadastroLayout.visibility == VISIBLE) {
             senhaCadastroLayout.setGone()
         } else {
             senhaCadastroLayout.setVisible()
         }

         if (confirmaSenhaCadastroLayout.visibility == VISIBLE) {
             confirmaSenhaCadastroLayout.setGone()
         } else {
             confirmaSenhaCadastroLayout.setVisible()
         }

         if (campoNomeEscolaCadastroLayout.visibility == VISIBLE) {
             campoNomeEscolaCadastroLayout.setGone()
         } else {
             campoNomeEscolaCadastroLayout.setVisible()
         }

         if (campoNomeCidadeCadastroLayout.visibility == VISIBLE) {
             campoNomeCidadeCadastroLayout.setGone()
         } else {
             campoNomeCidadeCadastroLayout.setVisible()
         }

         if (labelTipoEscola.visibility == VISIBLE) {
             labelTipoEscola.setGone()
         } else {
             labelTipoEscola.setVisible()
         }

         if (tipoEscolaCadastro.visibility == VISIBLE) {
             tipoEscolaCadastro.setGone()
         } else {
             tipoEscolaCadastro.setVisible()
         }

     }*/

}