package com.vadeen.dns.server

import com.vadeen.dns.getTestHeader
import com.vadeen.dns.getTestQuestion
import com.vadeen.dns.message.Header
import com.vadeen.dns.message.Message
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.given
import org.mockito.Mockito.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

internal class DnsClientTest {

    @Test
    internal fun testSend() {
        val socket = mock(DatagramSocket::class.java)
        val client = DnsClient(InetAddress.getByName("127.0.0.1"), 8053, socket)

        val testHeader = getTestHeader(1, 0, 0, 0)
        val testQuestion = getTestQuestion()
        client.send(Message(testHeader.obj, listOf(testQuestion.obj), emptyList(), emptyList(), emptyList()))

        val packetCaptor = ArgumentCaptor.forClass(DatagramPacket::class.java)
        verify(socket, times(1)).send(packetCaptor.capture())

        val packet = packetCaptor.value

        assertEquals(8053, packet.port)
        assertEquals(InetAddress.getByName("127.0.0.1"), packet.address)
        assertArrayEquals(testHeader.data + testQuestion.data, packet.data)
    }

    @Test
    internal fun testReceive() {
        val socket = mock(DatagramSocket::class.java)
        val client = DnsClient(InetAddress.getByName("127.0.0.1"), 8053, socket)

        val testHeader = getTestHeader(1, 0, 0, 0)
        val testQuestion = getTestQuestion()

        given(socket.receive(any())).willAnswer() {
            val packet = it.arguments[0] as DatagramPacket
            packet.data = testHeader.data + testQuestion.data
            Unit
        }
        val message = client.receive()

        val expected = Message(testHeader.obj, listOf(testQuestion.obj), emptyList(), emptyList(), emptyList())
        assertEquals(expected, message)
    }
}