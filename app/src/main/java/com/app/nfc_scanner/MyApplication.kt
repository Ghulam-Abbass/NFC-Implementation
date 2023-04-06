package com.app.nfc_scanner;

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.app.nfc_scanner.DI.components.AppComponents
import com.app.nfc_scanner.DI.components.DaggerAppComponents
import com.app.nfc_scanner.DI.module.AppModule
import com.app.nfc_scanner.Utils.NetUtil
import com.example.myapplication.Utils.Const
import com.example.myapplication.Utils.SharedPref

class MyApplication : MultiDexApplication(), LifecycleObserver, SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var component: AppComponents

    private lateinit var sp: SharedPref

    override fun onCreate() {
        super.onCreate()
        if (Const.IS_LIVE_MODE) {
            Const.BASED_URL = Const.BASE_LIVE_URL
        } else {
            Const.BASED_URL = Const.BASE_STAG_URL
        }
//        sp = SharedPref(this)
//        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        component = DaggerAppComponents.builder().appModule(AppModule(this)).build()
        getAppComponent(this).doInjection(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        NetUtil.registerPref(this, this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        fun getAppComponent(context: Context): AppComponents {
            return (context.applicationContext as MyApplication).component
        }
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
    }

}
