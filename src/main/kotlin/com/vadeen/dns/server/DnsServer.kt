package com.vadeen.dns.server

import com.vadeen.dns.io.DnsMessageReader
import com.vadeen.dns.io.DnsStreamReader
import java.net.DatagramPacket
import java.net.DatagramSocket

class DnsServer(private val socket: DatagramSocket) {

    fun receive(): DnsRequest {
        val buff = ByteArray(512)
        val packet = DatagramPacket(buff, buff.size)

        socket.receive(packet)

        val streamReader = DnsStreamReader.of(packet.data)
        val messageReader = DnsMessageReader(streamReader)
        val message = messageReader.readMessage()

        return DnsRequest(packet.address, packet.port, message)
    }
}