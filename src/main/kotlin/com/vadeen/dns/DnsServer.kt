package com.vadeen.dns

import com.vadeen.dns.io.DnsMessageReader
import com.vadeen.dns.io.DnsStreamReader
import com.vadeen.dns.message.Message
import java.net.DatagramPacket
import java.net.DatagramSocket

class DnsServer(private val socket: DatagramSocket) {

    fun receive(): Message {
        val buff = ByteArray(512)
        val packet = DatagramPacket(buff, buff.size)

        socket.receive(packet)

        val streamReader = DnsStreamReader.of(packet.data)
        val messageReader = DnsMessageReader(streamReader)

        return messageReader.readMessage()
    }
}