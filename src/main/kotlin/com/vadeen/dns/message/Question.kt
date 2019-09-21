package com.vadeen.dns.message

import com.vadeen.dns.constants.RecordClass
import com.vadeen.dns.constants.RecordType

data class Question(
    val name: List<ByteArray>,
    val recordType: RecordType,
    val recordClass: RecordClass
)
