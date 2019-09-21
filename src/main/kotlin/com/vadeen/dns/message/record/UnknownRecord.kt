package com.vadeen.dns.message.record

import com.vadeen.dns.constants.RecordClass
import com.vadeen.dns.constants.RecordType

class UnknownRecord(
    name: List<ByteArray>,
    recordType: RecordType,
    recordClass: RecordClass,
    ttl: Int,
    val data: ByteArray
) : Record(name, recordType, recordClass, ttl) {

    companion object {
        fun of(name: List<ByteArray>, rtype: RecordType, rclass: RecordClass, ttl: Int, data: ByteArray) =
            UnknownRecord(name, rtype, rclass, ttl, data)
    }
}
