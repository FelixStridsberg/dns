package com.vadeen.dns.io

import com.vadeen.dns.message.Question
import com.vadeen.dns.message.record.Record

class DnsMessageWriter(private val stream: DnsStreamWriter) {

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
}