package com.app.nfc_scanner.NFC.records

import android.nfc.NdefRecord
import com.google.common.base.Preconditions
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.*

class TextRecord(private val languageCode: String, private val text: String) : ParsedNdefRecord {

    override fun str(): String {
        return text
    }

    fun getText(): String {
        return text
    }

    fun getLanguageCode(): String {
        return languageCode
    }

    companion object {
        fun parse(record: NdefRecord): TextRecord {
            Preconditions.checkArgument(record.tnf == NdefRecord.TNF_WELL_KNOWN)
            Preconditions.checkArgument(Arrays.equals(record.type, NdefRecord.RTD_TEXT))
            return try {
                val payload: ByteArray = record.payload
                val textEncoding = if (payload[0].toInt() and 128 == 0) "UTF-8" else "UTF-16"
                val languageCodeLength = payload[0].toInt() and 63
                val languageCode = String(payload, 1, languageCodeLength, Charsets.US_ASCII)
                val text = String(
                    payload, languageCodeLength + 1,
                    payload.size - languageCodeLength - 1, Charset.forName(textEncoding)
                )
                TextRecord(languageCode, text)
            } catch (e: UnsupportedEncodingException) {
                // should never happen unless we get a malformed tag.
                throw IllegalArgumentException(e)
            }
        }

        fun isText(record: NdefRecord): Boolean {
            return try {
                parse(record)
                true
            } catch (e: IllegalArgumentException) {
                false
            }
        }
    }
}
