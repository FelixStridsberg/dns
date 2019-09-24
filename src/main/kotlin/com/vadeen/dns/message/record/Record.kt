package com.vadeen.dns.message.record

import com.vadeen.dns.constants.RecordClass
import com.vadeen.dns.constants.RecordType
import com.vadeen.dns.message.DomainName

abstract class Record(
    val name: DomainName,
    val recordType: RecordType,
    val recordClass: RecordClass,
    val ttl: Int
) {
    companion object {
        fun of(name: DomainName, rtype: RecordType, rclass: RecordClass, ttl: Int, data: ByteArray) : Record =
            when (rtype) {
                is RecordType.Unknown -> UnknownRecord.of(name, rtype, rclass, ttl, data)
                is RecordType.A -> ARecord.of(name, rtype, rclass, ttl, data)
            }
    }

    override fun toString(): String {
        return String.format("%-25s %-10d %-10s %-10s", name, ttl, recordClass, recordType)
    }

    abstract fun toBytes(): ByteArray
}
