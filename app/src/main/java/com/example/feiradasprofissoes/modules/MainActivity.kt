package com.example.feiradasprofissoes.modules

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.CheckBox
import android.widget.Toast
import com.example.feiradasprofissoes.R
import com.example.feiradasprofissoes.modules.login.view.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    private var mDatabase: DatabaseReference? = null

    private var checkBoxChecked: Boolean? = false

    val listener = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {}

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            checkBoxChecked =
                dataSnapshot.child("user").child(mAuth?.currentUser!!.uid).child("checkBoxChecked")
                    .getValue(Boolean::class.java)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        if (mAuth?.currentUser != null) {
            mDatabase?.addValueEventListener(listener)
        } else {
            Toast.makeText(this, "AQUI OH", Toast.LENGTH_SHORT).show()
        }


        sairButton.setOnClickListener {
            if (isNetworkAvailable()) {

            userLoggedOut(false)
            mAuth?.signOut()
            userLoggedOutChangeActivity()
            } else {
                val snack =
                    Snackbar.make(constraintLayoutLogin, "Você está sem conexão com a internet!", Snackbar.LENGTH_SHORT)
                snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                snack.show()
            }

        }

    }

    private fun userLoggedOut(isUserLoggedIn: Boolean) {
        mDatabase?.child("user")?.child(mAuth?.currentUser!!.uid)?.child("userLoggedIn")?.setValue(isUserLoggedIn)

        if (checkBoxChecked == true) {
            mDatabase?.child("user")?.child(mAuth?.currentUser!!.uid)?.child("checkBoxChecked")?.setValue(false)
//            mDatabase?.child("user")?.child(mAuth?.currentUser!!.uid)?.child("checkBoxMarked")?.setValue(false)
        }
    }

    private fun userLoggedOutChangeActivity() {
        if (mAuth?.currentUser == null) {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        super.onBackPressed()
    }

    override fun finish() {
        super.finish()
        mDatabase?.removeEventListener(listener)
    }
}


