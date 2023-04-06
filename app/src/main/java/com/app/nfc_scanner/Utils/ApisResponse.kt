package com.app.nfc_scanner.Utils

import com.app.nfc_scanner.model.Status
import io.reactivex.annotations.NonNull
import io.reactivex.annotations.Nullable

class ApisResponse<T> private constructor(
    val status: Status, @param:Nullable @field:Nullable
    val data: T?, @param:Nullable @field:Nullable
    val error: Throwable?
) {
    companion object {
        fun <T> loading(): ApisResponse<T> {
            return ApisResponse(Status.LOADING, null, null)
        }
        fun <T> notSuccess(): ApisResponse<T> {
            return ApisResponse(Status.NOT_SUCCESS, null, null)
        }

        fun <T> success(@NonNull data: T?): ApisResponse<T> {
            return ApisResponse(Status.SUCCESS, data, null)
        }

        fun <T> error(@NonNull error: Throwable): ApisResponse<T> {
            return ApisResponse(Status.ERROR, null, error)
        }

    }
}