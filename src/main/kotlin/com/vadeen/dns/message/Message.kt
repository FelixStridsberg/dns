package com.vadeen.dns.message

import com.vadeen.dns.message.record.Record

data class Message(
    val header: Header,
    val questions: List<Question>,
    val answerRecords: List<Record>,
    val authorityRecords: List<Record>,
    val additionalRecords: List<Record>
)
