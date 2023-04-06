package com.app.nfc_scanner.NFC.parser


import android.nfc.NdefMessage
import android.nfc.NdefRecord
import com.app.nfc_scanner.NFC.records.ParsedNdefRecord
import com.app.nfc_scanner.NFC.records.SmartPoster
import com.app.nfc_scanner.NFC.records.TextRecord
import com.app.nfc_scanner.NFC.records.UriRecord

object NdefMessageParser {
    fun parse(message: NdefMessage): List<ParsedNdefRecord> = getRecords(message.records)

    fun getRecords(records: Array<NdefRecord>): List<ParsedNdefRecord> = records.map { record ->
        when {
            UriRecord.isUri(record) -> UriRecord.parse(record)
            TextRecord.isText(record) -> TextRecord.parse(record)
            SmartPoster.isPoster(record) -> SmartPoster.parse(record)
            else -> object : ParsedNdefRecord {
                override fun str(): String = String(record.payload)
            }
        }
    }
}
