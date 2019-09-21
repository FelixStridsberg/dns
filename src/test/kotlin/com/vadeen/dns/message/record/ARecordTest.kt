package com.vadeen.dns.message.record

import com.vadeen.dns.constants.ResourceClass
import com.vadeen.dns.constants.ResourceType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

internal class ARecordTest {

    @Test
    internal fun testInvalidArguments() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            ARecord.of(emptyList(), ResourceType.A(), ResourceClass.IN(), 0, byteArrayOf(0x01, 0x02, 0x03))
        }
    }
}