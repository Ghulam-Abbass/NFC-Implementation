package com.example.myapplication.Utils

object Const {

    private const val AUTH_URL = "http://testapi.techenablers.info/api/auth/"
    //Stage
    const val BASE_STAG_URL = "https://staging.blacktieskis.com/"

    //Live
    const val BASE_LIVE_URL = "https://booknow.blacktieskis.com/"

    var BASED_URL = ""

    const val NFC = BASE_STAG_URL + "api/v1/search-inventory/{barcode}"

    const val PREF_KEY = "pref_key"

    // For JWT Login and Register
    const val LOGIN_URL = AUTH_URL + "login"
    const val REGISTER_URL = AUTH_URL + "register"
    const val PROFILE_URL = AUTH_URL + "profile"
    const val LOGIN_API = BASE_STAG_URL + "api/auth/login"
    const val KEY_IS_LOGIN = "is_logged_in_BTMobile"
    const val KEY_USER_OBJECT = "BTMobile_user"

    val IS_LIVE_MODE: Boolean = false

}