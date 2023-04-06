package com.app.nfc_scanner.Utils

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class ExecuteApis private constructor(
    private val mCtx: Context
){
    private var mRequestQueue: RequestQueue?

    val requestQueue: RequestQueue?
    get() {
        if (mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mCtx.applicationContext)
        }
        return mRequestQueue
    }

    fun <T> ApisVolleyRequest(req: Request<T>?){
        requestQueue!!.add(req)
    }

    companion object{
        private var mInstance: ExecuteApis? = null

        @Synchronized
        fun getInstance(context: Context): ExecuteApis? {
            if (mInstance == null){
                mInstance = ExecuteApis(context)
            }
            return mInstance
        }
    }

    init {
        mRequestQueue = requestQueue
    }
}