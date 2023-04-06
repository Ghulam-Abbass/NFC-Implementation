package com.app.nfc_scanner.DI.components

import com.app.btmobile.BASE.BaseActivity
import com.app.nfc_scanner.DI.module.AppModule
import com.app.nfc_scanner.DI.module.UtilsModule
import com.app.nfc_scanner.DI.module.ViewModelsModule
import com.app.nfc_scanner.MyApplication
import com.app.nfc_scanner.screens.NFCActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [UtilsModule::class, ViewModelsModule::class, AppModule::class])
interface AppComponents {
    fun doInjection(activity: MyApplication)
    fun doInjection(activity: BaseActivity)
    fun doInjection(activity: NFCActivity)
}