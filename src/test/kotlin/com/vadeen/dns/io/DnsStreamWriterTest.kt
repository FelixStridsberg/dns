package com.vadeen.dns.io

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

internal class DnsStreamWriterTest {

    private var stream = ByteArrayOutputStream()
    private var writer = DnsStreamWriter(stream)

    @BeforeEach
    internal fun setUp() {
        stream = ByteArrayOutputStream()
        writer = DnsStreamWriter(stream)
    }

    @Test
    internal fun testWriteByte() {
        writer.writeByte(0x70)

        assertTrue(byteArrayOf(0x70).contentEquals(stream.toByteArray()))
    }

    @Test
    internal fun testWriteShort() {
        writer.writeShort(2)

        assertTrue(byteArrayOf(0x00, 0x02).contentEquals(stream.toByteArray()))
    }

    @Test
    internal fun testWriteInt() {
        writer.writeInt(19088743)

        assertTrue(byteArrayOf(0x01, 0x23, 0x45, 0x67).contentEquals(stream.toByteArray()))
    }

    @Test
    internal fun testReadDomainNameLabels() {
        val expectedData =
            byteArrayOf(0x02) + "NS".toByteArray() + byteArrayOf(0x04) + "TEST".toByteArray() + byteArrayOf(0x00)

        writer.writeLabels(listOf("NS".toByteArray(), "TEST".toByteArray()))

        assertTrue(expectedData.contentEquals(stream.toByteArray()))
    }
}