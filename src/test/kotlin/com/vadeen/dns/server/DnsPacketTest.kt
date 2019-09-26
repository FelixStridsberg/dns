package com.vadeen.dns.server

import com.vadeen.dns.getTestHeader
import com.vadeen.dns.message.Message
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.net.InetAddress

internal class DnsPacketTest {

    @Test
    fun testReply() {
        val testHeader = getTestHeader(0, 0, 0, 0)
        val message = Message(testHeader.obj, emptyList(), emptyList(), emptyList(), emptyList())
        val message2 = Message(testHeader.obj, emptyList(), emptyList(), emptyList(), emptyList())

        val packet = DnsPacket(InetAddress.getByName("8.8.8.8"), 54, message)
        val replyPacket = packet.reply(message2)

        assertEquals(InetAddress.getByName("8.8.8.8"), replyPacket.address)
        assertEquals(54, replyPacket.port)
        assertFalse(replyPacket.message === packet.message)
    }
}