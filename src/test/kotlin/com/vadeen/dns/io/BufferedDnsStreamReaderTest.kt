package com.vadeen.dns.io

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

internal class BufferedDnsStreamReaderTest {

    @Test
    internal fun testReadByte() {
        val inputStream = ByteArrayInputStream(byteArrayOf(0x22))
        val dnsStreamReader = DnsStreamReader(inputStream)

        assertEquals(0x22, dnsStreamReader.readByte())
    }

    @Test
    internal fun testReadByteEndOfStream() {
        val inputStream = ByteArrayInputStream(byteArrayOf(0x22))
        val dnsStreamReader = DnsStreamReader(inputStream)

        dnsStreamReader.readByte()

        val exception = assertThrows(DnsIOException::class.java) { dnsStreamReader.readByte() }
        assertEquals("Unexpected end of stream.", exception.message)
    }

    @Test
    internal fun testReadShort() {
        val inputStream = ByteArrayInputStream(byteArrayOf(0x00, 0x02))
        val dnsStreamReader = DnsStreamReader(inputStream)

        assertEquals(2, dnsStreamReader.readShort())
    }

    @Test
    internal fun testReadInt() {
        val inputStream = ByteArrayInputStream(byteArrayOf(0x01, 0x23, 0x45, 0x67))
        val dnsStreamReader = DnsStreamReader(inputStream)

        assertEquals(19088743, dnsStreamReader.readInt())
    }

    @Test
    internal fun testReadShortEndOfStream() {
        val inputStream = ByteArrayInputStream(byteArrayOf(0x02, 0x00, 0x00))
        val dnsStreamReader = DnsStreamReader(inputStream)

        dnsStreamReader.readShort()

        val exception = assertThrows(DnsIOException::class.java) { dnsStreamReader.readShort() }
        assertEquals("Unexpected end of stream.", exception.message)
    }

    @Test
    internal fun testReadDomainName() {
            val inputStream = ByteArrayInputStream(
                byteArrayOf(0x02) + "NS".toByteArray() + byteArrayOf(0x04) + "TEST".toByteArray() + byteArrayOf(0x00)
            )
            val dnsStreamReader = DnsStreamReader(inputStream)

            val domainName = dnsStreamReader.readDomainName()
            assertEquals(2, domainName.size)
            assertTrue("NS".toByteArray().contentEquals(domainName[0]))
            assertTrue("TEST".toByteArray().contentEquals(domainName[1]))
    }

    @Test
    internal fun testReadDomainNameEndOfStream() {
        val inputStream = ByteArrayInputStream(
            byteArrayOf(0x02) + "NS".toByteArray() + byteArrayOf(0x04) + "TES".toByteArray()
        )
        val dnsStreamReader = DnsStreamReader(inputStream)

        val exception = assertThrows(DnsIOException::class.java) { dnsStreamReader.readDomainName() }
        assertEquals("Unexpected end of stream.", exception.message)
    }
}