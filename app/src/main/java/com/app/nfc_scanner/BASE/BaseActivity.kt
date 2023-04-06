package com.app.btmobile.BASE

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.app.nfc_scanner.R

open class BaseActivity : AppCompatActivity() {

    private var progressDialog: AlertDialog.Builder? = null
    private var alertDialog: AlertDialog? = null

    fun showProgressDialog(context: Context, text: String = "") {
        progressDialog = AlertDialog.Builder(context)
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val view: View = layoutInflater.inflate(R.layout.loader_layout, viewGroup, false)
        progressDialog?.setView(view)

//        if (text.isNotEmpty()) {
//            val textView: TextView = view.findViewById(R.id.tvTitle)
//            textView.text = text
//        }
        alertDialog = progressDialog?.create()
        alertDialog?.setCancelable(false)
        alertDialog?.show()
    }

    fun hideProgressDialog() {
        if (alertDialog?.isShowing == true) {
            alertDialog?.dismiss()
        }
    }

    private val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    fun isEmailValid(email: String): Boolean {
        return EMAIL_REGEX.toRegex().matches(email)
    }

}