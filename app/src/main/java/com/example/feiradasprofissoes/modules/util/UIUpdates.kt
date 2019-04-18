package com.example.feiradasprofissoes.modules.util

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View?.hideKeyboard(){
    this?.run {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager?
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }
}

fun View?.showKeyboard() {
    this?.run {
        val imm = this.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager?
        imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun View?.setVisible() {
    this?.let {
        if (it.visibility == View.VISIBLE) {
            return@let
        } else {
            it.visibility = View.VISIBLE
        }
    }
}

fun View?.setInvisible() {
    this?.let {
        if (it.visibility == View.INVISIBLE) {
            return@let
        } else {
            it.visibility = View.INVISIBLE
        }
    }
}

fun View?.setGone() {
    this?.let {
        if (it.visibility == View.GONE) {
            return@let
        } else {
            it.visibility = View.GONE
        }
    }
}