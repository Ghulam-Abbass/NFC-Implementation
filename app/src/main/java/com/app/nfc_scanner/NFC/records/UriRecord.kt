package com.app.nfc_scanner.NFC.records

import android.net.Uri
import android.nfc.NdefRecord
import com.google.common.base.Preconditions
import com.google.common.collect.ImmutableBiMap
import com.google.common.primitives.Bytes
import java.nio.charset.Charset
import java.util.*

class UriRecord(uri: Uri?) : ParsedNdefRecord {
    private val mUri: Uri
    override fun str(): String {
        return mUri.toString()
    }

    val uri: Uri
        get() = mUri

    init {
        mUri = Preconditions.checkNotNull(uri)!!
    }

    companion object {
        private const val TAG = "UriRecord"
        const val RECORD_TYPE = "UriRecord"

        private val URI_PREFIX_MAP = ImmutableBiMap.Builder<Byte, String>()
            .put(0x00, "")
            .put(0x01, "http://www.")
            .put(0x02, "https://www.")
            .put(0x03, "http://")
            .put(0x04, "https://")
            .put(0x05, "tel:")
            .put(0x06, "mailto:")
            .put(0x07, "ftp://anonymous:anonymous@")
            .put(0x08, "ftp://ftp.")
            .put(0x09, "ftps://")
            .put(0x0A, "sftp://")
            .put(0x0B, "smb://")
            .put(0x0C, "nfs://")
            .put(0x0D, "ftp://")
            .put(0x0E, "dav://")
            .put(0x0F, "news:")
            .put(0x10, "telnet://")
            .put(0x11, "imap:")
            .put(0x12, "rtsp://")
            .put(0x13, "urn:")
            .put(0x14, "pop:")
            .put(0x15, "sip:")
            .put(0x16, "sips:")
            .put(0x17, "tftp:")
            .put(0x18, "btspp://")
            .put(0x19, "btl2cap://")
            .put(0x1A, "btgoep://")
            .put(0x1B, "tcpobex://")
            .put(0x1C, "irdaobex://")
            .put(0x1D, "file://")
            .put(0x1E, "urn:epc:id:")
            .put(0x1F, "urn:epc:tag:")
            .put(0x20, "urn:epc:pat:")
            .put(0x21, "urn:epc:raw:")
            .put(0x22, "urn:epc:")
            .put(0x23, "urn:nfc:")
            .build()

        fun parse(record: NdefRecord): UriRecord {
            val tnf: Short = record.getTnf()
            if (tnf == NdefRecord.TNF_WELL_KNOWN) {
                return parseWellKnown(record)
            } else if (tnf == NdefRecord.TNF_ABSOLUTE_URI) {
                return parseAbsolute(record)
            }
            throw IllegalArgumentException("Unknown TNF $tnf")
        }

        private fun parseAbsolute(record: NdefRecord): UriRecord {
            val payload: ByteArray = record.getPayload()
            val uri: Uri = Uri.parse(String(payload, Charset.forName("UTF-8")))
            return UriRecord(uri)
        }

        private fun parseWellKnown(record: NdefRecord): UriRecord {
            Preconditions.checkArgument(Arrays.equals(record.getType(), NdefRecord.RTD_URI))
            val payload: ByteArray = record.getPayload()
            val prefix: String? = URI_PREFIX_MAP.get(payload[0])
            val fullUri: ByteArray = Bytes.concat(
                prefix?.toByteArray(Charset.forName("UTF-8")), Arrays.copyOfRange(
                    payload, 1,
                    payload.size
                )
            )
            val uri: Uri = Uri.parse(String(fullUri, Charset.forName("UTF-8")))
            return UriRecord(uri)
        }

        fun isUri(record: NdefRecord): Boolean {
            return try {
                parse(record)
                true
            } catch (e: IllegalArgumentException) {
                false
            }
        }

        private val EMPTY = ByteArray(0)
    }
}