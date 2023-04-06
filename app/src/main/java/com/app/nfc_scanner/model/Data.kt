package com.app.nfc_scanner.model

data class Data(
    val access_token: String,
    val expires_in: Int,
    val message: String,
    val token_type: String,
    val user: User
)