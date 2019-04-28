package com.example.feiradasprofissoes.modules.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment

fun Context.getPreferences(preferenceKey: String? = null): SharedPreferences {

    return preferenceKey?.let {
        this.getSharedPreferences(preferenceKey, Context.MODE_PRIVATE)
    } ?: kotlin.run {
        PreferenceManager.getDefaultSharedPreferences(this)
    }
}

fun Fragment.getPreferences(preferenceKey: String? = null) = activity?.getPreferences(preferenceKey)

@Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
fun <T> SharedPreferences.read(key: String, defaultValue: T): T {

    return when (defaultValue) {
        is Boolean -> getBoolean(key, defaultValue)
        is Int -> getInt(key, defaultValue)
        is Long -> getLong(key, defaultValue)
        is Float -> getFloat(key, defaultValue)
        is String -> getString(key, defaultValue)
        else -> throw IllegalArgumentException()
    } as T
}

@SuppressLint("ApplySharedPref")
fun SharedPreferences.write(param: Pair<String, Any>) {

    val editor = edit()

    val key = param.first
    val value = param.second

    when (value) {
        is Boolean -> editor.putBoolean(key, value)
        is Int -> editor.putInt(key, value)
        is Long -> editor.putLong(key, value)
        is Float -> editor.putFloat(key, value)
        is String -> editor.putString(key, value)
        else -> throw IllegalArgumentException()
    }

    editor.commit()

}

@SuppressLint("ApplySharedPref")
fun SharedPreferences.clearFromKey(key: String) {

    edit().remove(key).commit()
}

@SuppressLint("ApplySharedPref")
fun SharedPreferences.clearFromKeys(vararg keys: String) {
    val editor = edit()

    keys.forEach {
        editor.remove(it)
    }

    editor.commit()
}

