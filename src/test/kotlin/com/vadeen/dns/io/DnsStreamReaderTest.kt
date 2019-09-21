package com.vadeen.dns.io

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BufferedDnsStreamReaderTest {

    @Test
    internal fun testReadByte() {
        val dnsStreamReader = DnsStreamReader.of(byteArrayOf(0x22))

        assertEquals(0x22, dnsStreamReader.readByte())
    }

    @Test
    internal fun testReadByteEndOfStream() {
        val dnsStreamReader = DnsStreamReader.of(byteArrayOf(0x22))

        dnsStreamReader.readByte()

        val exception = assertThrows(DnsIOException::class.java) { dnsStreamReader.readByte() }
        assertEquals("Unexpected end of stream.", exception.message)
    }

    @Test
    internal fun testReadShort() {
        val dnsStreamReader = DnsStreamReader.of(byteArrayOf(0x00, 0x02))

        assertEquals(2, dnsStreamReader.readShort())
    }

    @Test
    internal fun testReadInt() {
        val dnsStreamReader = DnsStreamReader.of(byteArrayOf(0x01, 0x23, 0x45, 0x67))

        assertEquals(19088743, dnsStreamReader.readInt())
    }

    @Test
    internal fun testReadShortEndOfStream() {
        val dnsStreamReader = DnsStreamReader.of(byteArrayOf(0x02, 0x00, 0x00))

        dnsStreamReader.readShort()

        val exception = assertThrows(DnsIOException::class.java) { dnsStreamReader.readShort() }
        assertEquals("Unexpected end of stream.", exception.message)
    }

    @Test
    internal fun testReadDomainName() {
            val data =
                byteArrayOf(0x02) + "NS".toByteArray() + byteArrayOf(0x04) + "TEST".toByteArray() + byteArrayOf(0x00)
            val dnsStreamReader = DnsStreamReader.of(data)

            val domainName = dnsStreamReader.readDomainName()
            assertEquals(2, domainName.size)
            assertTrue("NS".toByteArray().contentEquals(domainName[0]))
            assertTrue("TEST".toByteArray().contentEquals(domainName[1]))
    }

    @Test
    internal fun testReadDomainNameEndOfStream() {
        val data =
            byteArrayOf(0x02) + "NS".toByteArray() + byteArrayOf(0x04) + "TES".toByteArray()
        val dnsStreamReader = DnsStreamReader.of(data)

        val exception = assertThrows(DnsIOException::class.java) { dnsStreamReader.readDomainName() }
        assertEquals("Unexpected end of stream.", exception.message)
    }
}