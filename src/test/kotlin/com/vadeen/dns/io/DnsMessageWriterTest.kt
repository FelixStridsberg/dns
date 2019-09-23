package com.vadeen.dns.io

import com.vadeen.dns.constants.OperationCode
import com.vadeen.dns.constants.RecordClass
import com.vadeen.dns.constants.RecordType
import com.vadeen.dns.constants.ResponseCode
import com.vadeen.dns.message.Header
import com.vadeen.dns.message.Question
import com.vadeen.dns.message.record.UnknownRecord
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.io.ByteArrayOutputStream

internal class DnsMessageWriterTest {

    private var outputStream = ByteArrayOutputStream()
    private var streamWriter = DnsStreamWriter(outputStream)
    private var writer = DnsMessageWriter(streamWriter)

    @BeforeEach
    internal fun setUp() {
        outputStream = ByteArrayOutputStream()
        streamWriter = DnsStreamWriter(outputStream)
        writer = DnsMessageWriter(streamWriter)
    }

    @Test
    internal fun testWriteHeader() {
        val header = Header(
            9999, true, OperationCode.Query(),
            authoritativeAnswer = false,
            truncation = true,
            recursionDesired = false,
            recursionAvailable = true,
            responseCode = ResponseCode.NoError(),
            questions = 1,
            answerRecords = 2,
            authorityRecords = 3,
            additionalRecords = 4
        )
        writer.writeHeader(header)

        val expectedOutput = byteArrayOf(
            0x027, 0x0F,                    // ID = 9999
            0x82.toByte(), 0x80.toByte(),   // Opcode, RCODE, flags.
            0x00, 0x01,                     // Questions
            0x00, 0x02,                     // Answer records
            0x00, 0x03,                     // Authority records
            0x00, 0x04                      // Additional records
        )
        val output = outputStream.toByteArray()!!
        assertEquals(expectedOutput.size, output.size)
        assertTrue(output.contentEquals(expectedOutput))
    }

    @Test
    internal fun testWriteHeaderReversed() {
        val header = Header(
            9999, false, OperationCode.of(0xf),
            authoritativeAnswer = true,
            truncation = false,
            recursionDesired = true,
            recursionAvailable = false,
            responseCode = ResponseCode.of(0xf),
            questions = 4,
            answerRecords = 3,
            authorityRecords = 2,
            additionalRecords = 1
        )
        writer.writeHeader(header)

        val expectedOutput = byteArrayOf(
            0x027, 0x0F,                    // ID = 9999
            0x7D.toByte(), 0x0f.toByte(),   // Opcode, RCODE, flags.
            0x00, 0x04,                     // Questions
            0x00, 0x03,                     // Answer records
            0x00, 0x02,                     // Authority records
            0x00, 0x01                      // Additional records
        )
        val output = outputStream.toByteArray()!!
        assertEquals(expectedOutput.size, output.size)
        assertTrue(output.contentEquals(expectedOutput))
    }

    @Test
    internal fun testWriteQuestion() {
        val name = listOf("ns".toByteArray(), "com".toByteArray())

        val question = Question(name, RecordType.of(600), RecordClass.of(600))
        writer.writeQuestion(question)

        val expectedOutput = byteArrayOf(
            0x02, 'n'.toByte(), 's'.toByte(), 0x03, 'c'.toByte(), 'o'.toByte(), 'm'.toByte(), 0x00,
            0x02, 0x58,     // Type=600
            0x02, 0x58      // Class=600
        )
        val output = outputStream.toByteArray()!!
        assertEquals(expectedOutput.size, output.size)
        assertTrue(output.contentEquals(expectedOutput))
    }

    @Test
    internal fun testWriteRecord() {
        val name = listOf("ns".toByteArray(), "com".toByteArray())
        val data = byteArrayOf(0x01, 0x02, 0x03, 0x04, 0x05)

        val record = UnknownRecord(name, RecordType.of(600), RecordClass.of(600), 100, data)
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