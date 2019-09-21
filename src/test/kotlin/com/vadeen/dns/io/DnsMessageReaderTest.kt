package com.vadeen.dns.io

import com.vadeen.dns.constants.OperationCode
import com.vadeen.dns.constants.ResourceClass
import com.vadeen.dns.constants.ResourceType
import com.vadeen.dns.constants.ResponseCode
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import kotlin.experimental.inv

internal class DnsMessageReaderTest {

    @Test
    internal fun testReadHeader() {
        val inputStream = ByteArrayInputStream(byteArrayOf(
            0x04, 0xB0.toByte(),    // ID=1200
            0x79.toByte(), 0x0F,    // QR=0, OPCODE=0xF, AA=0, TC=0, RD=1, RA=0, RCODE=0xF
            0x00, 0x01,             // Question records
            0x00, 0x10,             // Answer records
            0x01, 0x00,             // Authority records
            0x10, 0x00              // Additional records
        ))
        val dnsStreamReader = DnsStreamReader(inputStream)
        val dnsMessageReader = DnsMessageReader(dnsStreamReader)

        val header = dnsMessageReader.readHeader()
        assertEquals(1200, header.id)
        assertEquals(false, header.isResponse)
        assertEquals(OperationCode.Unknown(0xF), header.operationCode)
        assertEquals(false, header.authoritativeAnswer)
        assertEquals(false, header.truncation)
        assertEquals(true, header.recursionDesired)
        assertEquals(false, header.recursionAvailable)
        assertEquals(ResponseCode.Unknown(0xF), header.responseCode)
        assertEquals(1, header.questionRecords)
        assertEquals(16, header.answerRecords)
        assertEquals(256, header.authorityRecords)
        assertEquals(4096, header.additionalRecords)
    }

    @Test
    internal fun testReadHeaderReversed() {
        val inputStream = ByteArrayInputStream(byteArrayOf(
            0x00, 0x0C.toByte(),                // ID=12
            0x79.toByte().inv(), 0x0F.inv(),    // QR=1, OPCODE=0x0, AA=1, TC=1, RD=0, RA=1, RCODE=0x0
            0x00, 0x01,                         // Question records
            0x00, 0x10,                         // Answer records
            0x01, 0x00,                         // Authority records
            0x10, 0x00                          // Additional records
        ))
        val dnsStreamReader = DnsStreamReader(inputStream)
        val dnsMessageReader = DnsMessageReader(dnsStreamReader)

        val header = dnsMessageReader.readHeader()
        assertEquals(12, header.id)
        assertEquals(true, header.isResponse)
        assertEquals(OperationCode.Unknown(0x0), header.operationCode)
        assertEquals(true, header.authoritativeAnswer)
        assertEquals(true, header.truncation)
        assertEquals(false, header.recursionDesired)
        assertEquals(true, header.recursionAvailable)
        assertEquals(ResponseCode.Unknown(0x0), header.responseCode)
    }

    @Test
    internal fun readQuestion() {
        val inputStream = ByteArrayInputStream(byteArrayOf(
            0x02, 'n'.toByte(), 's'.toByte(), 0x03, 'c'.toByte(), 'o'.toByte(), 'm'.toByte(), 0x00, // QNAME=ns.com
            0x01, 0x00,     // QTYPE=256
            0x00, 0x10      // QCLASS=16
        ))
        val dnsStreamReader = DnsStreamReader(inputStream)
        val dnsMessageReader = DnsMessageReader(dnsStreamReader)

        val question = dnsMessageReader.readQuestion()
        assertEquals(2, question.name.size)
        assertTrue("ns".toByteArray().contentEquals(question.name[0]))
        assertTrue("com".toByteArray().contentEquals(question.name[1]))
        assertEquals(ResourceType.Unknown(256), question.resourceType)
        assertEquals(ResourceClass.Unknown(16), question.resourceClass)
    }
}