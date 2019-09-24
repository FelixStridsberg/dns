package com.vadeen.dns.message.record

import com.vadeen.dns.constants.RecordClass
import com.vadeen.dns.constants.RecordType
import com.vadeen.dns.message.DomainName

class UnknownRecord(
    name: DomainName,
    recordType: RecordType,
    recordClass: RecordClass,
    ttl: Int,
    private val data: ByteArray
) : Record(name, recordType, recordClass, ttl) {

    companion object {
        fun of(name: DomainName, rtype: RecordType, rclass: RecordClass, ttl: Int, data: ByteArray) =
            UnknownRecord(name, rtype, rclass, ttl, data)
    }

    override fun toBytes() = data

    override fun toString(): String {
        val record = super.toString()
        val dataLen = data.size

        return "$record data_len=$dataLen"
    }
}
