package com.vadeen.dns.message

import com.vadeen.dns.constants.OperationCode
import com.vadeen.dns.constants.ResponseCode

data class Header(
    val id: Int,
    val isResponse: Boolean,
    val operationCode: OperationCode,
    val authoritativeAnswer: Boolean,
    val truncation: Boolean,
    val recursionDesired: Boolean,
    val recursionAvailable: Boolean,
    val responseCode: ResponseCode,
    val questionRecords: Int,
    val answerRecords: Int,
    val authorityRecords: Int,
    val additionalRecords: Int
)
