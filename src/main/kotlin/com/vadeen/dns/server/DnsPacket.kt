package com.vadeen.dns.server

import com.vadeen.dns.message.Message
import java.net.InetAddress

data class DnsPacket(
    val address: InetAddress,
    val port: Int,
    val message: Message
) {
    fun reply(message: Message) = DnsPacket(address, port, message)
}
