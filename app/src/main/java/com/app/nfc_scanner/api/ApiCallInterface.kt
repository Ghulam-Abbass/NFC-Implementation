package com.app.nfc_scanner.api

import com.app.nfc_scanner.model.nfc_model.NfcModel
import com.example.myapplication.Utils.Const
import io.reactivex.Observable
import retrofit2.http.*

interface ApiCallInterface {
    @Headers("Accept: application/json")
    @GET(Const.NFC)
    fun hitNfcApi(
        @Header("Authorization") authorization: String,
        @Path("barcode") barcode: String,
        @Query("search_type") search_type: String
    ): Observable<NfcModel>
}