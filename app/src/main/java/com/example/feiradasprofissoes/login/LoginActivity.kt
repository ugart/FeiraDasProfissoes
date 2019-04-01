//package com.example.feiradasprofissoes.login
//
//import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
//import android.util.Log
//import android.widget.Toast
//import com.example.feiradasprofissoes.R
//import com.google.firebase.auth.FirebaseAuth
//
//class LoginActivity : AppCompatActivity() {
//
//    private val TAG = "FirebaseEmailPassword"
//
//    private var mAuth: FirebaseAuth? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//
//        mAuth = FirebaseAuth.getInstance()
//    }
//
//    override fun onStart() {
//        super.onStart()
//        val currentUser = mAuth!!.currentUser
//        updateUI(currentUser)
//    }
//
//    fun criarConta(email: String, password: String) {
//
//        if (!validateForm(email, password)) {
//            return
//        }
//
//        mAuth!!.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//
//                if (task.isSuccessful) {
//                    Log.e(TAG, "createAccount: Success!")
//
//                    val user = mAuth!!.currentUser
//                    updateUI(user)
//                } else {
//                    Log.e(TAG, "createAccount: Fail!", task.exception)
//                    Toast.makeText(applicationContext, "Authentication failed!", Toast.LENGTH_SHORT).show()
//                    updateUI(null)
//                }
//
//            }
//    }
//
//    fun signIn(email: String, password: String) {
//
//        if (!validateForm(email, password)) {
//            return
//        }
//
//        mAuth!!.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//
//                if (task.isSuccessful) {
//                    Log.e(TAG, "signIn: Success!")
//
//                    val user = mAuth!!.getCurrentUser()
//                    updateUI(user)
//                } else {
//                    Log.e(TAG, "signIn: Fail!", task.exception)
//                    Toast.makeText(applicationContext, "Authentication failed!", Toast.LENGTH_SHORT).show()
//                    updateUI(null)
//                }
//
////                if (!task.isSuccessful) {
////                    tvStatus.text = "Authentication failed!"
////                }
//            }
//    }
//
//    private fun signOut() {
//        mAuth!!.signOut()
//        updateUI(null)
//    }
//
//
//
//
//
//}
