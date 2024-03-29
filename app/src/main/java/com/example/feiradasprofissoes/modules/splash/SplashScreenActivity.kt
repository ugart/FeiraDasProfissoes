package com.example.feiradasprofissoes.modules.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.feiradasprofissoes.R
import com.example.feiradasprofissoes.modules.curso.MainActivity
import com.example.feiradasprofissoes.modules.login.view.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SplashScreenActivity : AppCompatActivity() {
    
    private var checkBoxChecked: Boolean? = false

    private var userLoggedIn: Boolean? = false

    private var mDatabase: DatabaseReference? = null

    private var mAuth: FirebaseAuth? = null

    val listener = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {}

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            checkBoxChecked =
                dataSnapshot.child("user").child(mAuth?.currentUser!!.uid).child("checkBoxChecked")
                    .getValue(Boolean::class.java)
            userLoggedIn = dataSnapshot.child("user").child(mAuth?.currentUser!!.uid).child("userLoggedIn")
                .getValue(Boolean::class.java)
        }

    }

    private fun exitSplashScreen(runnable: Runnable) {
        Handler().postDelayed(
            runnable,
            2500
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        if (mAuth?.currentUser != null) {
            mDatabase?.addValueEventListener(listener)
        } else {
            val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        if (mAuth?.currentUser == null) {
        } else {

            exitSplashScreen(Runnable {

                if (mAuth?.currentUser != null) {

                    if (checkBoxChecked != true) {
                        mDatabase?.child("user")?.child(mAuth?.currentUser!!.uid)?.child("userLoggedIn")
                            ?.setValue(false)
                        startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                        finish()
                    } else {

                        if (userLoggedIn == true) {
                            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                            finish()
                        } else {
                            mDatabase?.child("user")?.child(mAuth?.currentUser!!.uid)?.child("checkBoxChecked")
                                ?.setValue(false)
                            startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                            finish()
                        }

                    }

                } else {
                    startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                    finish()
                }

            })
        }
    }

    override fun finish() {
        super.finish()
        mDatabase?.removeEventListener(listener)
    }


}



