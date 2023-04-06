package com.app.nfc_scanner.Utils

import android.R
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager

class LoaderDialog(var context: Context) {
    lateinit var dialog: Dialog;
    fun initializeDialog(isCancelable: Boolean = true) {
        dialog = Dialog(context, R.style.Theme_Translucent_NoTitleBar)
        dialog = Dialog(context, R.style.Theme_Translucent_NoTitleBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(com.app.nfc_scanner.R.layout.loader_layout)
        dialog.setCancelable(isCancelable)
        val window = dialog.window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv()
        window.attributes = wlp
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    fun showDialog(showDialog: Boolean = true) {
        if (dialog != null && showDialog) {
            dialog!!.show()
        }
    }

    fun isLoaderVisible(): Boolean {
        if (dialog != null) {
            return dialog.isShowing
        }

        return true
    }

    fun dismissDialog() {
        if (dialog != null) {
            dialog!!.cancel()
        }
    }
}