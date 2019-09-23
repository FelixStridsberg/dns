package com.vadeen.dns.message

import com.vadeen.dns.constants.OperationCode
import com.vadeen.dns.constants.ResponseCode
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class HeaderTest {

    @Test
    internal fun testToString() {
        var header = Header(
            54321, true, OperationCode.Query(),
            false, false, true, true,
            ResponseCode.NoError(), 1, 2, 3, 4)

        assertEquals(
            "Header (id: 54321, opcode: QUERY, qr: true, aa: false, tc: false, rc: true, ra: true, rcode: NOERR)",
            header.toString()
        )
    }
}