package com.app.nfc_scanner.Utils

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.Utils.Const

object NetUtil {
    fun isHttpStatusCode(throwable: Throwable, statusCode: Int): Boolean {
        if (throwable is retrofit2.HttpException) {
            return throwable.code() === statusCode
        } else if (throwable is com.jakewharton.retrofit2.adapter.rxjava2.HttpException) {
            return throwable.code() === statusCode
        }
        return false
    }

    fun registerPref(context: Context, listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        val pref = context.getSharedPreferences(Const.PREF_KEY, Context.MODE_PRIVATE)
        pref.registerOnSharedPreferenceChangeListener(listener)
    }
}