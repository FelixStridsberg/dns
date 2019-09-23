package com.vadeen.dns.message

import com.vadeen.dns.constants.OperationCode
import com.vadeen.dns.constants.ResponseCode
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class HeaderTest {

    @Test
    internal fun testToString() {
        val header = Header(
            54321, true, OperationCode.Query(),
            authoritativeAnswer = false,
            truncation = false,
            recursionDesired = true,
            recursionAvailable = true,
            responseCode = ResponseCode.NoError(),
            questions = 1,
            answerRecords = 2,
            authorityRecords = 3,
            additionalRecords = 4
        )

        assertEquals(
            "Header (id: 54321, opcode: QUERY, qr: true, aa: false, tc: false, rc: true, ra: true, rcode: NOERR)\n" +
            "questions: 1, answers: 2, authorities: 3, additionals: 4",
            header.toString()
        )
    }
}