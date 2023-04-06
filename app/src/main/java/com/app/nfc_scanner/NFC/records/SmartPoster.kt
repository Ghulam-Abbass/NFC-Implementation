package com.app.nfc_scanner.NFC.records

import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import com.app.nfc_scanner.NFC.parser.NdefMessageParser
import com.google.common.collect.Iterables
import java.util.*

class SmartPoster(
    private val uriRecord: UriRecord,
    private val title: TextRecord?,
    private val action: RecommendedAction,
    private val type: String?
) : ParsedNdefRecord {

    override fun str(): String = title?.str() + "\n" + uriRecord.str() ?: uriRecord.str()

    enum class RecommendedAction(val byte: Byte) {
        UNKNOWN((-1).toByte()),
        DO_ACTION(0),
        SAVE_FOR_LATER(1),
        OPEN_FOR_EDITING(2);

        companion object {
            private val lookup = values().associateBy(RecommendedAction::byte)
            fun fromByte(byte: Byte): RecommendedAction = lookup[byte] ?: UNKNOWN
        }
    }

    companion object {
        fun parse(record: NdefRecord): SmartPoster {
            check(record.tnf == NdefRecord.TNF_WELL_KNOWN)
            check(Arrays.equals(record.type, NdefRecord.RTD_SMART_POSTER))
            return try {
                val subRecords = NdefMessage(record.payload)
                parse(subRecords.records)
            } catch (e: FormatException) {
                throw IllegalArgumentException(e)
            }
        }

        fun parse(recordsRaw: Array<NdefRecord>): SmartPoster {
            val records: Iterable<ParsedNdefRecord> = NdefMessageParser.getRecords(recordsRaw)
            val uri: UriRecord = Iterables.getOnlyElement(records.filterIsInstance<UriRecord>())
            val title: TextRecord? = getFirstIfExists(records, TextRecord::class.java)
            val action = parseRecommendedAction(recordsRaw)
            val type = parseType(recordsRaw)
            return SmartPoster(uri, title, action, type)
        }

        fun isPoster(record: NdefRecord?): Boolean {
            return try {
                parse(record!!)
                true
            } catch (e: IllegalArgumentException) {
                false
            }
        }

        private fun <T> getFirstIfExists(elements: Iterable<*>, type: Class<T>): T? {
            val filtered: Iterable<T> = elements.filterIsInstance(type)
            return if (filtered.any()) filtered.first() else null
        }

        private val ACTION_RECORD_TYPE = byteArrayOf('a'.toByte(), 'c'.toByte(), 't'.toByte())
        private fun parseRecommendedAction(records: Array<NdefRecord>): RecommendedAction {
            val record: NdefRecord? = getByType(ACTION_RECORD_TYPE, records)
            return if (record != null) {
                val action: Byte = record.payload[0]
                RecommendedAction.fromByte(action)
            } else RecommendedAction.UNKNOWN
        }

        private val TYPE_TYPE = byteArrayOf('t'.toByte())
        private fun parseType(records: Array<NdefRecord>): String? {
            val type: NdefRecord? = getByType(TYPE_TYPE, records)
            return type?.let { String(it.payload, Charsets.UTF_8) }
        }

        private fun getByType(type: ByteArray, records: Array<NdefRecord>): NdefRecord? {
            return records.find { Arrays.equals(type, it.type) }
        }
    }
}