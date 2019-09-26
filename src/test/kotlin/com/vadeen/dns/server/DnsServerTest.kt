package com.vadeen.dns.server

import com.vadeen.dns.getTestHeader
import com.vadeen.dns.getTestQuestion
import com.vadeen.dns.message.Message
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.mock
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

internal class DnsServerTest {

    /*
    @Test
    internal fun test() {
        val server = DnsServer(8080)

        val client = DnsClient(InetAddress.getByName("127.0.0.53"), 53)

        // Get message from DNS server.
        val message = server.receive()

        // Send message to a real DNS server.
        client.send(message)

        // Wait for response form real DNS server.
        val clientReply = client.receive()

        // Create a reply containing the real DNS reply.
        message.reply(clientReply)

        // Send reply.
        server.send(reply)
    }
     */

    @Test
    internal fun testServerSend() {
        val socket = mock(DatagramSocket::class.java)

        val testHeader = getTestHeader(1, 0, 0, 0)
        val testQuestion = getTestQuestion()
        val testMessage = Message(testHeader.obj, listOf(testQuestion.obj), emptyList(), emptyList(), emptyList())

        val server = DnsServer(socket)
        server.send(DnsPacket(InetAddress.getByName("127.0.0.1"), 53, testMessage))

        val packetCaptor = ArgumentCaptor.forClass(DatagramPacket::class.java)
        Mockito.verify(socket, Mockito.times(1)).send(packetCaptor.capture())

        val packet = packetCaptor.value

        assertEquals(InetAddress.getByName("127.0.0.1"), packet.address)
        assertEquals(53, packet.port)
        assertArrayEquals(testHeader.data + testQuestion.data, packet.data)
    }

    @Test
    internal fun testServerReceive() {
        val socket = mock(DatagramSocket::class.java)

        val testHeader = getTestHeader(1, 0, 0, 0)
        val testQuestion = getTestQuestion()
        val testMessage = Message(testHeader.obj, listOf(testQuestion.obj), emptyList(), emptyList(), emptyList())

        given(socket.receive(any())).willAnswer {
            val packet = it.arguments[0] as DatagramPacket
            packet.address = InetAddress.getByName("127.0.0.1")
            packet.port = 53
            packet.data = testHeader.data + testQuestion.data
            Unit
        }

        val server = DnsServer(socket)
        val request = server.receive()
        val message = request.message

        assertEquals(InetAddress.getByName("127.0.0.1"), request.address)
        assertEquals(53, request.port)
        assertEquals(testMessage, message)
    }
}