package com.example.feiradasprofissoes.modules.util

import android.app.Dialog
import android.content.Context

class DialogCustom(context: Context, themeResId: Int) : Dialog(context) {

    private var callback: AlertDialogCallback? = null

    interface AlertDialogCallback {
        fun onDismiss()
    }

    fun setAlertDialogListener(callback: AlertDialogCallback): DialogCustom {
        this.callback = callback
        return this
    }

    override fun dismiss() {
        super.dismiss()

        if (callback != null) {
            callback!!.onDismiss()
        }

    }

}