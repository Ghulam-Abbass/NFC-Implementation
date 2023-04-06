package com.app.nfc_scanner.model.nfc_model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NfcModel(
    val status: String,
    val `object`: BarcodeData?
)

data class BarcodeData(
    var barcode: String,
    val id: Int,
    val resortId: Int,
    var name: String?,
    val manufacture: String?,
    var status: String?,
    val type: String?,
    var model: String?,
    val length: String?,
    var size: String?,
    var search_type: String?,
    val inventory_location_name: String?,
    val packages_resort: List<String>?,
    @SerializedName("broken_at")
    @Expose
    val broken_at: String,
    @SerializedName("detail")
    @Expose
    val detail: Detail?,
    @SerializedName("addon")
    @Expose
    val addon: NfcAddon?
)

data class Detail(
    val reservation_name: String,
    val reservation_id: String,
    val renter_name: String,
    val return_date: String,
)

data class NfcAddon(
    val id: String,
    val name: String,
    val resort_id: String,
    val manufacture: String?,
    val model: String?,
    val barcode: String?,
)