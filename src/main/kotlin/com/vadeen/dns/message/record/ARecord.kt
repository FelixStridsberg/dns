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

    override fun toBytes() = ip

    override fun toString(): String {
        val record = super.toString()

        // Hack to make the octets unsigned without using the experimental UByte.
        val o1 = byteToInt(ip[0])
        val o2 = byteToInt(ip[1])
        val o3 = byteToInt(ip[2])
        val o4 = byteToInt(ip[3])

        val fullName = name.joinToString(separator = ".") { String(it) }
        return String.format("$record $o1.$o2.$o3.$o4",
            fullName, ttl, recordClass, recordType)
    }

    private fun byteToInt(byte: Byte) = byte.toInt() and 0xff
}