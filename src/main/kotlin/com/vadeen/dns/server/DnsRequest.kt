package com.vadeen.dns.server

import com.vadeen.dns.message.Message
import java.net.InetAddress

data class DnsRequest(
    val address: InetAddress,
    val port: Int,
    val message: Message
)
