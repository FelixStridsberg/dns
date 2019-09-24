package com.vadeen.dns

import com.vadeen.dns.constants.OperationCode
import com.vadeen.dns.constants.RecordClass
import com.vadeen.dns.constants.RecordType
import com.vadeen.dns.constants.ResponseCode
import com.vadeen.dns.message.DomainName
import com.vadeen.dns.message.Header
import com.vadeen.dns.message.Question

data class TestData<E>(val obj: E, val data: ByteArray)

fun getTestHeader(questions: Int, answers: Int, authorities: Int, additionals: Int): TestData<Header> {
    val obj = Header(
        1200, false, OperationCode.Query(),
        authoritativeAnswer = false,
        truncation = false,
        recursionDesired = true,
        recursionAvailable = false,
        responseCode = ResponseCode.NoError(),
        questions = questions,
        answerRecords = answers,
        authorityRecords = authorities,
        additionalRecords = additionals
    )

    val data = byteArrayOf(
        0x04, 0xB0.toByte(),    // ID=1200
        0x01.toByte(), 0x00,    // QR=0, OPCODE=0, AA=0, TC=0, RD=1, RA=0, RCODE=0
        (questions shr 8).toByte(), questions.toByte(),
        (answers shr 8).toByte(), answers.toByte(),
        (authorities shr 8).toByte(), authorities.toByte(),
        (additionals shr 8).toByte(), additionals.toByte()
    )

    return TestData(obj, data)
}

fun getTestHeaderInverse(questions: Int, answers: Int, authorities: Int, additionals: Int): TestData<Header> {
    val obj = Header(
        12, true, OperationCode.Unknown(0x0F),
        authoritativeAnswer = true,
        truncation = true,
        recursionDesired = false,
        recursionAvailable = true,
        responseCode = ResponseCode.Unknown(0x0E),
        questions = questions,
        answerRecords = answers,
        authorityRecords = authorities,
        additionalRecords = additionals
    )

    val data = byteArrayOf(
        0x00, 0x0C.toByte(),            // ID=12
        0xFE.toByte(), 0x8E.toByte(),   // QR=1, OPCODE=0xF, AA=1, TC=1, RD=0, RA=1, RCODE=0xE
        (questions shr 8).toByte(), questions.toByte(),
        (answers shr 8).toByte(), answers.toByte(),
        (authorities shr 8).toByte(), authorities.toByte(),
        (additionals shr 8).toByte(), additionals.toByte()
    )

    return TestData(obj, data)
}

fun getTestQuestion(): TestData<Question> {
    val obj = Question(DomainName.of("ns.com"), RecordType.Unknown(256), RecordClass.of(16))
    val data = byteArrayOf(
        0x02, 'n'.toByte(), 's'.toByte(), 0x03, 'c'.toByte(), 'o'.toByte(), 'm'.toByte(), 0x00, // QNAME=ns.com
        0x01, 0x00,     // QTYPE=256
        0x00, 0x10      // QCLASS=16
    )

    return TestData(obj, data)
}

