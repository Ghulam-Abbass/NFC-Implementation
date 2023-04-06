package com.app.nfc_scanner.repository

import com.app.nfc_scanner.api.ApiCallInterface
import com.app.nfc_scanner.model.nfc_model.NfcModel
import io.reactivex.Observable

class Repository(private val apiCallInterface: ApiCallInterface) {

    fun hitNfc(auth: String, barcode: String, search_type: String): Observable<NfcModel> {
        return apiCallInterface.hitNfcApi(auth, barcode, search_type)
    }
}