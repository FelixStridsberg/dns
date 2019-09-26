package com.vadeen.dns.server

import com.vadeen.dns.io.DnsMessageReader
import com.vadeen.dns.io.DnsMessageWriter
import com.vadeen.dns.io.DnsStreamReader
import com.vadeen.dns.io.DnsStreamWriter
import java.io.ByteArrayOutputStream
import java.net.DatagramPacket
import java.net.DatagramSocket

class DnsServer(private val socket: DatagramSocket) {

    fun receive(): DnsPacket {
        val buff = ByteArray(512)
        val packet = DatagramPacket(buff, buff.size)

        socket.receive(packet)

        val streamReader = DnsStreamReader.of(packet.data)
        val messageReader = DnsMessageReader(streamReader)
        val message = messageReader.readMessage()

        return DnsPacket(packet.address, packet.port, message)
    }

    fun send(packet: DnsPacket) {
        val output = ByteArrayOutputStream()
        val streamWriter = DnsStreamWriter(output)
        val writer = DnsMessageWriter(streamWriter)
        writer.writeMessage(packet.message)

        val data = output.toByteArray()
        socket.send(DatagramPacket(data, data.size, packet.address, packet.port))
    }
}