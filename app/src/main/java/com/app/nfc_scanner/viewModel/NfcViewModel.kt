package com.app.nfc_scanner.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.nfc_scanner.Utils.ApisResponse
import com.app.nfc_scanner.Utils.Debugger
import com.app.nfc_scanner.Utils.NetUtil.isHttpStatusCode
import com.app.nfc_scanner.model.nfc_model.NfcModel
import com.app.nfc_scanner.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NfcViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val disposables = CompositeDisposable()
    private val responseNfcLiveData = MutableLiveData<ApisResponse<NfcModel>>()

    fun nfcScanResponse(): MutableLiveData<ApisResponse<NfcModel>> {
        return responseNfcLiveData
    }

    fun hitNfcScan(auth: String, barcode: String, search_type: String) {
        Debugger.wtf("hitNfcScan", "$auth / $barcode $search_type")
        disposables.add(
            repository.hitNfc("Bearer $auth", barcode, search_type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    responseNfcLiveData.setValue(ApisResponse.loading())
                }
                .subscribe(
                    {
                        responseNfcLiveData.setValue(ApisResponse.success(it))
                    },
                    { e ->
                        if (isHttpStatusCode(e, 400) || isHttpStatusCode(e, 401)) {
                            responseNfcLiveData.setValue(ApisResponse.notSuccess())
                        } else {
                            responseNfcLiveData.setValue(ApisResponse.error(e))
                        }
                    }
                ))
    }
}