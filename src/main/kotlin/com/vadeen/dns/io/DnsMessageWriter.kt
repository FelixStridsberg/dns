package com.vadeen.dns.io

import com.vadeen.dns.message.record.Record

class DnsMessageWriter(private val stream: DnsStreamWriter) {

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