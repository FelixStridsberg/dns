package com.vadeen.dns.io

import com.vadeen.dns.message.Header
import com.vadeen.dns.message.Message
import com.vadeen.dns.message.Question
import com.vadeen.dns.message.record.Record

/**
 * Current limitation:
 * Optional domain system compression scheme not implemented (https://tools.ietf.org/html/rfc1035#section-4.1.4)
 */
class DnsMessageWriter(private val stream: DnsStreamWriter) {

    fun writeMessage(message: Message) {
        writeHeader(message.header)

        message.questions.forEach { writeQuestion(it) }
        message.answerRecords.forEach { writeRecord(it) }
        message.authorityRecords.forEach { writeRecord(it) }
        message.additionalRecords.forEach { writeRecord(it) }
    }

    /**
     * Ref: https://tools.ietf.org/html/rfc1035#section-4.1.1
     */
    fun writeHeader(header: Header) {
        stream.writeShort(header.id)
        stream.writeByte(getFlagByte1(header))
        stream.writeByte(getFlagByte2(header))
        stream.writeShort(header.questions)
        stream.writeShort(header.answerRecords)
        stream.writeShort(header.authorityRecords)
        stream.writeShort(header.additionalRecords)
    }

    /**
     * Ref: https://tools.ietf.org/html/rfc1035#section-4.1.2
     */
    fun writeQuestion(question: Question) {
        stream.writeLabels(question.name)
        stream.writeShort(question.recordType.value)
        stream.writeShort(question.recordClass.value)
    }

    /**
     * Ref: https://tools.ietf.org/html/rfc1035#section-4.1.3
     */
    fun writeRecord(record: Record) {
        stream.writeLabels(record.name)
        stream.writeShort(record.recordType.value)
        stream.writeShort(record.recordClass.value)
        stream.writeInt(record.ttl)

        val data = record.toBytes()
        stream.writeShort(data.size)
        stream.writeBytes(data)
    }

    /**
     * The byte containing QR, Opcode, AA, TC and RD.
     */
    private fun getFlagByte1(header: Header): Byte {
        val opcode = header.operationCode.value.toInt()

        var byte1 = ((opcode shl 3) and 0x78)
        if (header.isResponse)
            byte1 = byte1 or 0x80

        if (header.authoritativeAnswer)
            byte1 = byte1 or 0x04

        if (header.truncation)
            byte1 = byte1 or 0x02

        if (header.recursionDesired)
            byte1 = byte1 or 0x01

        return byte1.toByte()
    }

    /**
     * The byte containing RA and RCODE.
     */
    private fun getFlagByte2(header: Header): Byte {
        var byte = header.responseCode.value.toInt() and 0x0F

        if (header.recursionAvailable)
            byte = byte or 0x80

        return byte.toByte()
    }
}