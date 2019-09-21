package com.vadeen.dns.message

import com.vadeen.dns.constants.OperationCode
import com.vadeen.dns.constants.ResponseCode

data class Header(
    val id: Short,
    val isResponse: Boolean,
    val operationCode: OperationCode,
    val authoritativeAnswer: Boolean,
    val truncation: Boolean,
    val recursionDesired: Boolean,
    val recursionAvailable: Boolean,
    val responseCode: ResponseCode,
    val questionRecords: Short,
    val answerRecords: Short,
    val authorityRecords: Short,
    val additionalRecords: Short
)
