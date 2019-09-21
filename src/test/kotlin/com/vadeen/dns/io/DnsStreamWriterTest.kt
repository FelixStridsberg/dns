package com.vadeen.dns.io

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

internal class DnsStreamWriterTest {

    @Test
    internal fun testWriteByte() {
        val stream = ByteArrayOutputStream()
        val writer = DnsStreamWriter(stream)

        writer.writeByte(0x70)

        assertTrue(byteArrayOf(0x70).contentEquals(stream.toByteArray()))
    }

    @Test
    internal fun testWriteShort() {
        val stream = ByteArrayOutputStream()
        val writer = DnsStreamWriter(stream)

        writer.writeShort(2)

        assertTrue(byteArrayOf(0x00, 0x02).contentEquals(stream.toByteArray()))
    }

    @Test
    internal fun testWriteInt() {
        val stream = ByteArrayOutputStream()
        val writer = DnsStreamWriter(stream)

        writer.writeInt(19088743)

        assertTrue(byteArrayOf(0x01, 0x23, 0x45, 0x67).contentEquals(stream.toByteArray()))
    }
}