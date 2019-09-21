package com.vadeen.dns.message.record

import com.vadeen.dns.constants.RecordClass
import com.vadeen.dns.constants.RecordType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

internal class ARecordTest {

    @Test
    internal fun testInvalidArguments() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            ARecord.of(emptyList(), RecordType.A(), RecordClass.IN(), 0, byteArrayOf(0x01, 0x02, 0x03))
        }

        assertEquals("A Record data size must be 4.", exception.message)
    }

    @Test
    internal fun testToString() {
        val names = listOf("ns".toByteArray(), "vadeen".toByteArray(), "com".toByteArray())
        val ip = byteArrayOf(0x0A, 0x01, 0x01, 0xFF.toByte())
        val record = ARecord.of(names, RecordType.A(), RecordClass.IN(), 5444, ip)

        assertEquals("ns.vadeen.com             5444       IN         A          10.1.1.255", record.toString())
    }
}