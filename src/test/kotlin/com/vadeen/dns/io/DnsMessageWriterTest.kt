package com.vadeen.dns.io

import com.vadeen.dns.constants.RecordClass
import com.vadeen.dns.constants.RecordType
import com.vadeen.dns.message.record.UnknownRecord
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.ByteArrayOutputStream

internal class DnsMessageWriterTest {

    @Test
    fun writeRecord() {
        val name = listOf("ns".toByteArray(), "com".toByteArray())
        val data = byteArrayOf(0x01, 0x02, 0x03, 0x04, 0x05)

        val record = UnknownRecord(name, RecordType.of(600), RecordClass.of(600), 100, data)

        val outputStream = ByteArrayOutputStream()
        val streamWriter = DnsStreamWriter(outputStream)
        val writer = DnsMessageWriter(streamWriter)
        writer.writeRecord(record)

        val expectedOutput = byteArrayOf(
            0x02, 'n'.toByte(), 's'.toByte(), 0x03, 'c'.toByte(), 'o'.toByte(), 'm'.toByte(), 0x00,
            0x02, 0x58,                         // Type=600
            0x02, 0x58,                         // Class=600
            0x00, 0x00, 0x00, 0x64,             // Ttl=100
            0x00, 0x05, 0x01, 0x02, 0x03, 0x04, 0x05  // Data
        )
        val output = outputStream.toByteArray()!!
        assertEquals(expectedOutput.size, output.size)
        assertTrue(output.contentEquals(expectedOutput))
    }
}