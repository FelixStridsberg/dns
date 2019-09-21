package com.vadeen.dns.message.record

import com.vadeen.dns.constants.RecordClass
import com.vadeen.dns.constants.RecordType

abstract class Record(
    val name: List<ByteArray>,
    val recordType: RecordType,
    val recordClass: RecordClass,
    val ttl: Int
) {
    companion object {
        fun of(name: List<ByteArray>, rtype: RecordType, rclass: RecordClass, ttl: Int, data: ByteArray) : Record =
            when (rtype) {
                is RecordType.Unknown -> UnknownRecord.of(name, rtype, rclass, ttl, data)
                is RecordType.A -> ARecord.of(name, rtype, rclass, ttl, data)
            }
    }

    override fun toString(): String {
        val fullName = name.joinToString(separator = ".") { String(it) }
        return String.format("%-25s %-10d %-10s %-10s",
            fullName, ttl, recordClass, recordType)
    }
}
