package com.app.nfc_scanner.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.nfc_scanner.R
import com.app.nfc_scanner.Utils.Debugger
import com.app.nfc_scanner.Utils.ExecuteApis
import com.app.nfc_scanner.Utils.LoaderDialog
import com.app.nfc_scanner.databinding.ActivityLoginBinding
import com.app.nfc_scanner.model.ApiResponse
import com.app.nfc_scanner.model.AppUser
import com.app.nfc_scanner.model.Data
import com.app.nfc_scanner.model.User
import com.example.myapplication.Utils.Const
import com.example.myapplication.Utils.Const.LOGIN_API
import com.example.myapplication.Utils.Functions
import com.example.myapplication.Utils.SharedPref
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

@Suppress("all")
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var context = this
    private lateinit var sp: SharedPref
    private var TAG = "Response"
//    lateinit var loaderDialog: LoaderDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()

        initUI()
    }

    private fun initUI() {
        sp = SharedPref(context)
        clickListeners()
    }

    private fun clickListeners() {
        binding.btnLogin.setOnClickListener {
            getDataFromUser()
        }
    }

    private fun getDataFromUser() {

        // thi is use to get email and password from user

        val email = binding.etEmail.text.toString().trim()
        val pwd = binding.etPwd.text.toString().trim()


        // THERE IS SOME CONDITION WE APPLY ON OUR FIELDS

        if (email.isEmpty()) {
            Toast.makeText(context, "Please enter your email address", Toast.LENGTH_SHORT).show()
            return
        }

        if (pwd.isEmpty()) {
            Toast.makeText(context, "Please enter your password", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Functions.isValidEmailAddress(email)) {
            Toast.makeText(context, "Your entered email is invalid", Toast.LENGTH_SHORT).show()
            return
        }

        if (pwd.length < 6) {
            Toast.makeText(context, "Please entered password is weak", Toast.LENGTH_SHORT).show()
            return
        }

        // Now this function is take the user email and password

        loginUser(email, pwd)
    }

    private fun loginUser(email: String, pwd: String) {
        val jsonObject = JSONObject()
        jsonObject.put("email", email)
        jsonObject.put("password", pwd)

        val request = JsonObjectRequest(
            Request.Method.POST, LOGIN_API, jsonObject,
            { response ->
                renderResponse(response)
            },
            { error ->
                Toast.makeText(context,"Invalid Credentials",Toast.LENGTH_SHORT).show()
                Debugger.wtf(TAG, "Error: " + error.message)
            }
        )

        Volley.newRequestQueue(this).add(request)
    }

    private fun renderResponse(response: JSONObject?) {
        Debugger.wtf(TAG, "socketResponse:  ${response.toString()}")
        response?.let { res ->
            try {
                if (res.has("data") && res.getJSONObject("data").getString("message")
                        .contentEquals("Logged In successfully.")
                ) {
                    val appUser = AppUser()
                    val data = res.getJSONObject("data").getJSONObject("user")
                    appUser.id = data.getInt("id")
                    appUser.password = binding.etPwd.text.toString()
                    appUser.accessToken = res.getJSONObject("data").getString("access_token")
                    appUser.email = binding.etEmail.text.toString()
                    appUser.firstName = data.getString("first_name")
                    appUser.lastName = data.getString("last_name")
                    sp.saveProfile(appUser)
                    val i = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(i)
                    finish()
                } else {
                    Toast.makeText(context,"Invalid Credentials",Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Debugger.wtf(TAG, "Error ${e.message}")
            }
        }
    }


}