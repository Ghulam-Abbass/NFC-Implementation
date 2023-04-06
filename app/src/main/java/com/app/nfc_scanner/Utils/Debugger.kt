package com.app.nfc_scanner.Utils

import android.util.Log
import com.example.myapplication.Utils.Const.IS_LIVE_MODE

object Debugger {

    var IS_DEVELOPMENT_MODE = !IS_LIVE_MODE

    fun wtf(tag: String, msg: String?) {
        if (IS_DEVELOPMENT_MODE)
            Log.wtf(tag, msg)
    }
}