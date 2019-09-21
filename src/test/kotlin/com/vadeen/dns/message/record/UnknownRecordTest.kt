package com.vadeen.dns.message.record

import com.vadeen.dns.constants.RecordClass
import com.vadeen.dns.constants.RecordType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class UnknownRecordTest {

    @Test
    internal fun testToString() {
        val names = listOf("ns".toByteArray(), "vadeen".toByteArray(), "com".toByteArray())
        val bytes = byteArrayOf(0x0A, 0x01, 0x01, 0xFF.toByte(), 0x00, 0x00, 0x00)
        val record = UnknownRecord.of(names, RecordType.Unknown(99), RecordClass.IN(), 5444, bytes)

        assertEquals("ns.vadeen.com             5444       IN         U(99)      data_len=7", record.toString())
    }
}