package com.example.feiradasprofissoes.modules

import android.app.Application
import android.os.StrictMode

class ApplicationCreate : Application() {

    override fun onCreate() {
        super.onCreate()

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }

}