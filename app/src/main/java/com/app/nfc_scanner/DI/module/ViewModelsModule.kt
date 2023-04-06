package com.app.nfc_scanner.DI.module

import androidx.lifecycle.ViewModel
import com.app.nfc_scanner.viewModel.NfcViewModel
import com.ogoul.kalamtime.di.keys.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(NfcViewModel::class)
    abstract fun bindNfcScanViewModel(viewModel: NfcViewModel): ViewModel
}