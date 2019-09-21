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
}