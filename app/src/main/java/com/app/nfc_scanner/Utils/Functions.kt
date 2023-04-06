package com.example.myapplication.Utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import java.util.regex.Pattern

object Functions {

    fun changeTaskBarColor(context: Context, color: Int) {
        val window = (context as Activity).window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(context, color)
    }

    fun startActivityWithFlags(context: Context, mClass: Class<*>) {
        (context as Activity).finish()
        context.startActivity(
            Intent(context, mClass)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }

    fun startActivity(context: Context, mClass: Class<*>?) {
        context.startActivity(Intent(context, mClass))
    }

    fun startActivityWithData(context: Context, mClass: Class<*>?, gson: Gson, type: Any) {
        context.startActivity(
            Intent(context, mClass)
                .putExtra("send_data", gson.toJson(type))
        )
    }

    fun getIntentValue(context: Context, gson: Gson, nClass: Class<*>?): Any {
        val data = (context as Activity).intent.getStringExtra("send_data")
        return gson.fromJson(data, nClass)
    }

    fun isValidEmailAddress(email: String?): Boolean {
        val ePattern =
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
        val p = Pattern.compile(ePattern)
        val m = p.matcher(email)
        return m.matches()
    }

    fun setColor(context: Context, color: Int): Int {
        return ContextCompat.getColor(context, color)
    }

    fun showToast(activity: Activity, msg: String, isSuccess: Boolean = false) {
        if (!msg.isNullOrEmpty() && !msg.isNullOrBlank()) {
            if (isSuccess) {
                Toasty.success(activity, "$msg", Toast.LENGTH_LONG, false).show();
            } else {
                Toasty.error(activity, "$msg", Toast.LENGTH_LONG, false).show()
            }
        }
    }

    fun openNfcSettings(activity: Activity) {
        showToast(activity, "You need to enable NFC")
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        activity.startActivity(intent)
    }
}