package com.vadeen.dns.io

import com.vadeen.dns.constants.OperationCode
import com.vadeen.dns.constants.RecordClass
import com.vadeen.dns.constants.RecordType
import com.vadeen.dns.constants.ResponseCode
import com.vadeen.dns.message.DomainName
import com.vadeen.dns.message.Header
import com.vadeen.dns.message.Message
import com.vadeen.dns.message.Question
import com.vadeen.dns.message.record.Record

class DnsMessageReader(private val stream: DnsStreamReader) {

    /**
     * Reads message in the format:
     * +-----------------------+
     * |         Header        |
     * +-----------------------+
     * /       Questions       /
     * +-----------------------+
     * /     Answer records    /
     * +-----------------------+
     * /   Authority records   /
     * +-----------------------+
     * /   Additional records  /
     * +-----------------------+
     */
    fun readMessage(): Message {
        val header = readHeader()

        val questions = List(header.questions) { readQuestion() }
        val answerRecords = List(header.answerRecords) { readRecord() }
        val authorityRecords = List(header.authorityRecords) { readRecord() }
        val additionalRecords = List(header.additionalRecords) { readRecord() }

        return Message(header, questions, answerRecords, authorityRecords, additionalRecords)
    }

    /**
     * Reads message header in the format:
     *                                 1  1  1  1  1  1
     *   0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5
     * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     * |                      ID                       |
     * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     * |QR|   Opcode  |AA|TC|RD|RA|   Z    |   RCODE   |
     * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     * |                    QDCOUNT                    |
     * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     * |                    ANCOUNT                    |
     * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     * |                    NSCOUNT                    |
     * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     * |                    ARCOUNT                    |
     * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *
     * Ref: https://tools.ietf.org/html/rfc1035#section-4.1.1
     */
    fun readHeader(): Header {
        // First row.
        val id = stream.readShort()

        // Second row.
        val byte1 = stream.readRawByte()
        val response = byte1 and 0x80 != 0
        val operationCode = OperationCode.of(((byte1 shr 3) and 0x0F).toByte())
        val authoritativeAnswer = byte1 and 0x04 != 0
        val truncation = byte1 and 0x02 != 0
        val recursionDesired = byte1 and 0x01 != 0

        val byte2 = stream.readRawByte()
        val recursionAvailable = byte2 and 0x80 != 0
        val responseCode = ResponseCode.of((byte2 and 0x0F).toByte())

        // Third to sixth row.
        val questionRecords = stream.readShort()
        val answerRecords = stream.readShort()
        val authorityRecords = stream.readShort()
        val additionalRecords = stream.readShort()

        return Header(
            id, response, operationCode, authoritativeAnswer, truncation, recursionDesired, recursionAvailable,
            responseCode, questionRecords, answerRecords, authorityRecords, additionalRecords
        )
    }

    /**
     * Reads question in the format:
     *                                 1  1  1  1  1  1
     *   0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5
     * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     * /                     QNAME                     /
     * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     * |                     QTYPE                     |
     * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     * |                     QCLASS                    |
     * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *
     * Ref: https://tools.ietf.org/html/rfc1035#section-4.1.2
     */
    fun readQuestion(): Question {
        val domainName = DomainName(stream.readDomainName())
        val recordType = RecordType.of(stream.readShort())
        val recordClass = RecordClass.of(stream.readShort())

        return Question(domainName, recordType, recordClass)
    }

    /**
     * Reads resource record in the format:
     *                                 1  1  1  1  1  1
     *   0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5
     * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     * /                      NAME                     /
     * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     * |                      TYPE                     |
     * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     * |                     CLASS                     |
     * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     * |                      TTL                      |
     * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     * |                   RDLENGTH                    |
     * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--|
     * /                     RDATA                     /
     * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *
     * Ref: https://tools.ietf.org/html/rfc1035#section-4.1.3
     */
    fun readRecord(): Record {
        val name = DomainName(stream.readDomainName())
        val recordType = RecordType.of(stream.readShort())
        val recordClass = RecordClass.of(stream.readShort())
        val ttl = stream.readInt()
        val dataLen = stream.readShort()
        val data = stream.readBytes(dataLen)

        return Record.of(name, recordType, recordClass, ttl, data)
    }
}