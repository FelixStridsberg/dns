package com.vadeen.dns

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import java.net.DatagramPacket
import java.net.DatagramSocket

internal class DnsServerTest {

    @Test
    internal fun testServerReceive() {
        val socket = mock(DatagramSocket::class.java)

        given(socket.receive(any())).willAnswer {
            val packet = it.arguments[0] as DatagramPacket
            packet.data = simpleQuestion()
            Unit
        }

        val server = DnsServer(socket)
        val message = server.receive()

        assertEquals(1, message.questions.size)
        assertEquals(0, message.answerRecords.size)
        assertEquals(0, message.authorityRecords.size)
        assertEquals(0, message.additionalRecords.size)
    }

    private fun simpleQuestion(): ByteArray {
        val header = byteArrayOf(
            0x04, 0xB0.toByte(),    // ID=1200
            0x01.toByte(), 0x00,    // QR=0, OPCODE=0, AA=0, TC=0, RD=1, RA=0, RCODE=0
            0x00, 0x01,             // Question records = 1
            0x00, 0x00,             // Answer records = 0
            0x00, 0x00,             // Authority records = 0
            0x00, 0x00              // Additional records = 0
        )

        val question = byteArrayOf(
            0x02, 'n'.toByte(), 's'.toByte(), 0x03, 'c'.toByte(), 'o'.toByte(), 'm'.toByte(), 0x00, // QNAME=ns.com
            0x00, 0x01,     // QTYPE=A
            0x00, 0x01      // QCLASS=IN
        )

        return header + question
    }
}