package com.example.feiradasprofissoes.modules.util

import android.content.Context
import android.net.ConnectivityManager

object ConnectionUtils {

    private fun getConnectivityService(ctx: Context): ConnectivityManager? {
        return ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    fun isConnectedToInternet(context: Context): Boolean {

        val service = getConnectivityService(context)

        return service != null && service.activeNetworkInfo != null && service.activeNetworkInfo.isConnected

    }

}