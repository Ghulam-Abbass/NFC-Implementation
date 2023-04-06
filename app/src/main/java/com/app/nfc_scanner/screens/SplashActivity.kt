package com.app.nfc_scanner.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.app.nfc_scanner.MyApplication
import com.app.nfc_scanner.R
import com.app.nfc_scanner.Utils.Debugger
import com.example.myapplication.Utils.SharedPref
import com.google.gson.Gson

class SplashActivity : AppCompatActivity() {

    private lateinit var sp: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()
        sp = SharedPref(this)
        intent = Intent(this, LoginActivity::class.java)
        Handler().postDelayed({
            startActivity(intent)
            finish()
        }, 1000)
    }

}
