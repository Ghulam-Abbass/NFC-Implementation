package com.app.nfc_scanner.model

import android.util.Log
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AppUser : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("first_name")
    @Expose
    var firstName: String? = null

    @SerializedName("last_name")
    @Expose
    var lastName: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("access_token")
    @Expose
    var accessToken: String? = null
        get() {
            return field
        }
    var password: String? = null
}