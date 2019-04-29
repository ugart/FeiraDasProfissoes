package com.example.feiradasprofissoes.modules

import android.app.Application
import android.os.StrictMode
import com.google.firebase.database.FirebaseDatabase

class ApplicationCreate : Application() {

    override fun onCreate() {
        super.onCreate()

        
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }

}