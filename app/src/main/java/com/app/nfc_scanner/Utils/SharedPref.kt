package com.example.myapplication.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Base64
import android.util.Log
import com.app.nfc_scanner.model.ApiResponse
import com.app.nfc_scanner.model.AppUser
import com.app.nfc_scanner.model.Data
import com.app.nfc_scanner.model.SaveCounter
import com.google.gson.Gson
import javax.inject.Singleton

@Singleton
class SharedPref(var context: Context) {
    fun saveString(key: String, value: String) {
        val editor = context.getSharedPreferences("data", Context.MODE_PRIVATE).edit()
        editor.putString(key, value)
        editor.apply()
    }
    fun getString(key: String): String? {
        val sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, "")
    }
    fun saveImg(key: String, image: ByteArray) {
        val imgStr = Base64.encodeToString(image, Base64.DEFAULT)
        val sh = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = sh.edit()
        editor.putString(key, imgStr)
        editor.apply()
    }
    fun getImg(key: String): ByteArray {
        val sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val s = sharedPreferences.getString(key, "")
        // String text = new String(data, StandardCharsets.UTF_8);
        return Base64.decode(s, Base64.DEFAULT)
    }
    fun clear() {
        val settings = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        settings.edit().clear().apply()
    }
    fun saveBoolean(key: String, value: Boolean) {
        val prefs = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }
    fun getBoolean(key: String): Boolean {
        val prefs = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        return prefs.getBoolean(key, false)
    }
    fun saveInt(key: String, value: Int) {
        val prefs = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String): Int {
        val prefs = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        return prefs.getInt(key, 0)
    }

    fun saveProfile(login: AppUser) {
        saveString("user", Gson().toJson(login))
    }

    fun getProfile(): AppUser? {
        val data = getString("user")
        return if (data == null)
            null
        else
            Gson().fromJson(data, AppUser::class.java)
    }
}