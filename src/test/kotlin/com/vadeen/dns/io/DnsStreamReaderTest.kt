package com.vadeen.dns.io

import com.vadeen.dns.exception.DnsIOException
import com.vadeen.dns.exception.DnsParseException
import com.vadeen.dns.exception.NotImplementedException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class DnsStreamReaderTest {

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
    internal fun testReadDomainNameLabels() {
            val data =
                byteArrayOf(0x02) + "NS".toByteArray() + byteArrayOf(0x04) + "TEST".toByteArray() + byteArrayOf(0x00)
            val dnsStreamReader = DnsStreamReader.of(data)

            val domainName = dnsStreamReader.readDomainName()
            assertEquals(2, domainName.size)
            assertTrue("NS".toByteArray().contentEquals(domainName[0]))
            assertTrue("TEST".toByteArray().contentEquals(domainName[1]))
    }

    @Test
    internal fun testReadDomainNameReservedPointerSignature() {
        val data = byteArrayOf(0x80.toByte())
        val dnsStreamReader = DnsStreamReader.of(data)
        val exception = assertThrows(NotImplementedException::class.java) { dnsStreamReader.readDomainName() }

        assertEquals("Invalid label type.", exception.message)
    }

    @Test
    internal fun testReadDomainNamePointer() {
        val data =
            // Bogus data:
            byteArrayOf(0x00, 0x00, 0x00) +
            // Actual name (offset 0x03):
            byteArrayOf(0x02) + "NS".toByteArray() + byteArrayOf(0x04) + "TEST".toByteArray() + byteArrayOf(0x00) +
            // The pointer to the label at 0x03:
            byteArrayOf(0xC0.toByte(), 0x03)

        val dnsStreamReader = DnsStreamReader.of(data)
        dnsStreamReader.readBytes(12) // Read passed the label

        val domainName = dnsStreamReader.readDomainName()
        assertEquals(2, domainName.size)
        assertTrue("NS".toByteArray().contentEquals(domainName[0]))
        assertTrue("TEST".toByteArray().contentEquals(domainName[1]))
    }

    /**
     * Test read a label with a pointer to another pointer.
     * I don't think this is allowed.
     * If it is allowed there must be a limit of recursions to avoid dead locks.
     */
    @Test
    internal fun testReadRecursiveDomainNamePointer() {
        val data = byteArrayOf(0xC0.toByte(), 0x02, 0XC0.toByte(), 0x00)

        val dnsStreamReader = DnsStreamReader.of(data)
        val exception = assertThrows(DnsParseException::class.java) { dnsStreamReader.readDomainName() }

        assertEquals("Recursive label pointers not allowed.", exception.message)
    }

    @Test
    internal fun testReadDomainNameLabelsAndPointer() {
        val data =
            byteArrayOf(0x04) + "TEST".toByteArray() + byteArrayOf(0x00) +
            byteArrayOf(0x02) + "NS".toByteArray() +
            byteArrayOf(0xC0.toByte(), 0x00)

        val dnsStreamReader = DnsStreamReader.of(data)
        dnsStreamReader.readBytes(6) // Read passed the first label

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