package com.vadeen.dns.message

import com.vadeen.dns.constants.RecordClass
import com.vadeen.dns.constants.RecordType

data class Question(
    val name: List<ByteArray>,
    val recordType: RecordType,
    val recordClass: RecordClass
) {
    override fun toString(): String {
        val fullName = name.joinToString(separator = ".") { String(it) }
        return String.format("%-36s %-10s %s", fullName, recordClass, recordType)
    }
}
