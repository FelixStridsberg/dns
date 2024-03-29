package com.vadeen.dns.io

import com.vadeen.dns.constants.RecordClass
import com.vadeen.dns.constants.RecordType
import com.vadeen.dns.constants.ResponseCode
import com.vadeen.dns.getTestHeader
import com.vadeen.dns.getTestHeaderInverse
import com.vadeen.dns.getTestQuestion
import com.vadeen.dns.message.record.ARecord
import com.vadeen.dns.message.record.UnknownRecord
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class DnsMessageReaderTest {

    @Test
    internal fun testReadHeader() {
        val testHeader = getTestHeader(1, 16, 256, 4096)

        val dnsStreamReader = DnsStreamReader.of(testHeader.data)
        val dnsMessageReader = DnsMessageReader(dnsStreamReader)

        val header = dnsMessageReader.readHeader()
        assertEquals(testHeader.obj, header)
    }

    @Test
    internal fun testReadHeaderInverse() {
        val testHeader = getTestHeaderInverse(4096, 256, 16, 1)

        val dnsStreamReader = DnsStreamReader.of(testHeader.data)
        val dnsMessageReader = DnsMessageReader(dnsStreamReader)

        val header = dnsMessageReader.readHeader()
        assertEquals(testHeader.obj, header)
    }

    @Test
    internal fun readQuestion() {
        val testQuestion = getTestQuestion()

        val dnsStreamReader = DnsStreamReader.of(testQuestion.data)
        val dnsMessageReader = DnsMessageReader(dnsStreamReader)

        val question = dnsMessageReader.readQuestion()
        assertEquals(testQuestion.obj, question)
    }

    @Test
    internal fun readRecord() {
        val data = byteArrayOf(
            0x02, 'n'.toByte(), 's'.toByte(), 0x03, 'c'.toByte(), 'o'.toByte(), 'm'.toByte(), 0x00, // NAME=ns.com
            0x01, 0x00,                     // TYPE=256
            0x00, 0x10,                     // CLASS=16
            0x00, 0x00, 0x0E, 0x10,         // TTL=3600
            0x00, 0x03, 0x01, 0x02, 0x03    // DATA=0x010203
        )

        val dnsStreamReader = DnsStreamReader.of(data)
        val dnsMessageReader = DnsMessageReader(dnsStreamReader)

        val record = dnsMessageReader.readRecord()
        assertTrue(record is UnknownRecord)

        record as UnknownRecord

        assertEquals(2, record.name.labels.size)
        assertTrue("ns".toByteArray().contentEquals(record.name.labels[0]))
        assertTrue("com".toByteArray().contentEquals(record.name.labels[1]))
        assertEquals(RecordType.Unknown(256), record.recordType)
        assertEquals(RecordClass.Unknown(16), record.recordClass)
        assertEquals(3600, record.ttl)
        assertTrue(byteArrayOf(0x01, 0x02, 0x03).contentEquals(record.toBytes()))
    }

    @Test
    internal fun readARecord() {
        val data = byteArrayOf(
            0x02, 'n'.toByte(), 's'.toByte(), 0x03, 'c'.toByte(), 'o'.toByte(), 'm'.toByte(), 0x00, // NAME=ns.com
            0x00, 0x01,                         // TYPE=1 (A)
            0x00, 0x01,                         // CLASS=1 (IN)
            0x00, 0x00, 0x0E, 0x10,             // TTL=3600
            0x00, 0x04, 0x0A, 0x02, 0x03, 0x04  // IP=10.2.3.4
        )

        val dnsStreamReader = DnsStreamReader.of(data)
        val dnsMessageReader = DnsMessageReader(dnsStreamReader)

        val record = dnsMessageReader.readRecord()
        assertTrue(record is ARecord)

        record as ARecord

        assertEquals(2, record.name.labels.size)
        assertTrue("ns".toByteArray().contentEquals(record.name.labels[0]))
        assertTrue("com".toByteArray().contentEquals(record.name.labels[1]))
        assertEquals(RecordType.A(), record.recordType)
        assertEquals(RecordClass.IN(), record.recordClass)
        assertEquals(3600, record.ttl)
        assertTrue(byteArrayOf(0x0A, 0x02, 0x03, 0x04).contentEquals(record.ip))
        assertTrue(record.toBytes().contentEquals(record.ip))
    }

    @Test
    internal fun testReadMessage() {
        val header = byteArrayOf(
            0x04, 0xB0.toByte(),    // ID=1200
            0x01.toByte(), 0x00,    // QR=0, OPCODE=0, AA=0, TC=0, RD=1, RA=0, RCODE=0
            0x00, 0x02,             // Question records = 2
            0x00, 0x02,             // Answer records = 2
            0x00, 0x01,             // Authority records = 1
            0x00, 0x01              // Additional records = 1
        )

        val question = byteArrayOf(
            0x02, 'n'.toByte(), 's'.toByte(), 0x03, 'c'.toByte(), 'o'.toByte(), 'm'.toByte(), 0x00, // QNAME=ns.com
            0x01, 0x00,     // QTYPE=256
            0x00, 0x10      // QCLASS=16
        )
        val question1 = question.clone()
        val question2 = question.clone()

        question1[2] = '1'.toByte()
        question2[2] = '2'.toByte()

        val record = byteArrayOf(
            0x02, 'n'.toByte(), 's'.toByte(), 0x03, 'c'.toByte(), 'o'.toByte(), 'm'.toByte(), 0x00, // NAME=ns.com
            0x00, 0x01,                         // TYPE=1 (A)
            0x00, 0x01,                         // CLASS=1 (IN)
            0x00, 0x00, 0x0E, 0x10,             // TTL=3600
            0x00, 0x04, 0x0A, 0x02, 0x03, 0x04  // IP=10.2.3.4
        )
        val record1 = record.clone()
        val record2 = record.clone()
        val record3 = record.clone()
        val record4 = record.clone()

        record1[2] = '1'.toByte()
        record2[2] = '2'.toByte()
        record3[2] = '3'.toByte()
        record4[2] = '4'.toByte()

        val data = header + question1 + question2 + record1 + record2 + record3 + record4
        val dnsStreamReader = DnsStreamReader.of(data)
        val dnsMessageReader = DnsMessageReader(dnsStreamReader)

        val message = dnsMessageReader.readMessage()
        assertEquals(ResponseCode.NoError(), message.header.responseCode)
        assertEquals(2, message.questions.size)
        assertEquals(2, message.answerRecords.size)
        assertEquals(1, message.authorityRecords.size)
        assertEquals(1, message.additionalRecords.size)

        assertEquals("n1", String(message.questions[0].name.labels[0]))
        assertEquals("n2", String(message.questions[1].name.labels[0]))

        assertEquals("n1", String(message.answerRecords[0].name.labels[0]))
        assertEquals("n2", String(message.answerRecords[1].name.labels[0]))

        assertEquals("n3", String(message.authorityRecords[0].name.labels[0]))
        assertEquals("n4", String(message.additionalRecords[0].name.labels[0]))
    }
}