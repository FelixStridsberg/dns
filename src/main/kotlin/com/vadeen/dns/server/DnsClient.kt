package com.vadeen.dns.server

import com.vadeen.dns.io.DnsMessageReader
import com.vadeen.dns.io.DnsMessageWriter
import com.vadeen.dns.io.DnsStreamReader
import com.vadeen.dns.io.DnsStreamWriter
import com.vadeen.dns.message.Message
import java.io.ByteArrayOutputStream
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

/**
 * The dns client can communicate with a dns server.
 */
class DnsClient(
    private val address: InetAddress,
    private val port: Int,
    private val socket: DatagramSocket = DatagramSocket()
) {
    fun send(message: Message) {
        val output = ByteArrayOutputStream()
        val streamWriter = DnsStreamWriter(output)
        val writer = DnsMessageWriter(streamWriter)
        writer.writeMessage(message)

        val data = output.toByteArray()
        socket.send(DatagramPacket(data, data.size, address, port))
    }

    fun receive(): Message {
        val buff = ByteArray(512)
        val packet = DatagramPacket(buff, buff.size, address, port)

        socket.receive(packet)

        val streamReader = DnsStreamReader.of(packet.data)
        val messageReader = DnsMessageReader(streamReader)
        return messageReader.readMessage()
    }
}