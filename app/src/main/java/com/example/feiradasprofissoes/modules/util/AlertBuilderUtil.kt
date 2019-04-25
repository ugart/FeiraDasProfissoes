package com.example.feiradasprofissoes.modules.util

import android.content.Context
import android.support.annotation.StringRes
import com.example.feiradasprofissoes.R
import kotlinx.android.synthetic.main.alert_box_ok.*
import kotlinx.android.synthetic.main.alert_box_problem.*

class AlertBuilderUtil {

    fun alertBuilder(@StringRes resMensagem: Int, tipo: Type, contexto: Context): DialogCustom {
        val mensagem = contexto.getString(resMensagem)
        return alertBuilder(mensagem, tipo, contexto)
    }

    private fun alertBuilder(mensagem: String, tipo: Type, contexto: Context): DialogCustom {
        val dialog = DialogCustom(contexto, R.style.alert)

        when (tipo) {
            AlertBuilderUtil.Type.SUCCESS -> {
                dialog.setContentView(R.layout.alert_box_ok)
                val textSucess = dialog.mensagemOk
                textSucess.text = mensagem
            }
            AlertBuilderUtil.Type.ERROR -> {
                dialog.setContentView(R.layout.alert_box_problem)
                val textError = dialog.mensagemErro
                textError.text = mensagem
            }
        }
        dialog.show()
        return dialog
    }

    enum class Type {
        SUCCESS,
        ERROR
    }

}