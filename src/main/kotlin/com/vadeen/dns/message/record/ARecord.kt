package com.vadeen.dns.message.record

import com.vadeen.dns.constants.RecordClass
import com.vadeen.dns.constants.RecordType

class ARecord(
    name: List<ByteArray>,
    recordType: RecordType,
    recordClass: RecordClass,
    ttl: Int,
    val ip: ByteArray
) : Record(name, recordType, recordClass, ttl) {

    init {
        require(ip.size == 4) { "A Record data size must be 4." }
    }

    companion object {
        fun of(name: List<ByteArray>, rtype: RecordType, rclass: RecordClass, ttl: Int, data: ByteArray) =
            ARecord(name, rtype, rclass, ttl, data)
    }

}