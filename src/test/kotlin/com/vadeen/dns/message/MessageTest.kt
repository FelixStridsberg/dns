package com.vadeen.dns.message

import com.vadeen.dns.constants.OperationCode
import com.vadeen.dns.constants.RecordClass
import com.vadeen.dns.constants.RecordType
import com.vadeen.dns.constants.ResponseCode
import com.vadeen.dns.message.record.ARecord
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MessageTest {

    @Test
    fun testToString() {
        val header = Header(
            54321, true, OperationCode.Query(),
            authoritativeAnswer = false,
            truncation = false,
            recursionDesired = true,
            recursionAvailable = true,
            responseCode = ResponseCode.NoError(),
            questions = 1,
            answerRecords = 2,
            authorityRecords = 3,
            additionalRecords = 4
        )
        val name = listOf("ns".toByteArray(), "vadeen".toByteArray(), "com".toByteArray())
        val question = Question(name, RecordType.A(), RecordClass.IN())

        val ip = byteArrayOf(0x0A, 0x01, 0x01, 0xFF.toByte())
        val record = ARecord.of(name, RecordType.A(), RecordClass.IN(), 5444, ip)

        val message = Message(header, listOf(question), listOf(record), listOf(record), listOf(record))

        assertEquals(
            "Header (id: 54321, opcode: QUERY, qr: true, aa: false, tc: false, rc: true, ra: true, rcode: NOERR)\n" +
                    "questions: 1, answers: 2, authorities: 3, additionals: 4\n" +
                    "\n" +
                    "Questions:\n" +
                    "ns.vadeen.com                        IN         A\n" +
                    "\n" +
                    "Answers:\n" +
                    "ns.vadeen.com             5444       IN         A          10.1.1.255\n" +
                    "\n" +
                    "Authorities:\n" +
                    "ns.vadeen.com             5444       IN         A          10.1.1.255\n" +
                    "\n" +
                    "Additional:\n" +
                    "ns.vadeen.com             5444       IN         A          10.1.1.255\n",
                    message.toString()
        )
    }

    @Test
    fun testToStringNoRecords() {
        val header = Header(
            54321, true, OperationCode.Query(),
            authoritativeAnswer = false,
            truncation = false,
            recursionDesired = true,
            recursionAvailable = true,
            responseCode = ResponseCode.NoError(),
            questions = 1,
            answerRecords = 2,
            authorityRecords = 3,
            additionalRecords = 4
        )

        val message = Message(header, emptyList(), emptyList(), emptyList(), emptyList())

        assertEquals(
            "Header (id: 54321, opcode: QUERY, qr: true, aa: false, tc: false, rc: true, ra: true, rcode: NOERR)\n" +
                    "questions: 1, answers: 2, authorities: 3, additionals: 4\n",
            message.toString()
        )
    }
}