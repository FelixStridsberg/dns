package com.vadeen.dns.message

import com.vadeen.dns.constants.RecordClass
import com.vadeen.dns.constants.RecordType

data class Question(
    val name: DomainName,
    val recordType: RecordType,
    val recordClass: RecordClass
) {
    override fun toString(): String {
        return String.format("%-36s %-10s %s", name, recordClass, recordType)
    }
}
