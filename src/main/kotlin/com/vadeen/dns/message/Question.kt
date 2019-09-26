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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false

        other as Question

        if (name != other.name) return false
        if (recordType != other.recordType) return false
        if (recordClass != other.recordClass) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + recordType.hashCode()
        result = 31 * result + recordClass.hashCode()
        return result
    }
}
