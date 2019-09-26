package com.vadeen.dns

import com.vadeen.dns.server.DnsClient
import com.vadeen.dns.server.DnsServer
import java.net.DatagramSocket
import java.net.InetAddress

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Arguments: port [forwardHost:127.0.0.53] [forwardPort:53]")
        return
    }

    val port = args[0].toInt()
    val forwardHost = if (args.size > 1) InetAddress.getByName(args[1]) else InetAddress.getByName("127.0.0.53")
    val forwardPort = if (args.size > 2) args[2].toInt() else 53

    val server = DnsServer(DatagramSocket(port))
    val forwardClient = DnsClient(forwardHost, forwardPort)

    while (true) {
        val packet = server.receive()

        println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        println("Got question:")
        println(packet.message)

        forwardClient.send(packet.message)
        val forwardReply = forwardClient.receive()

        println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
        println("Got answer:")
        println(forwardReply)

        val reply = packet.reply(forwardReply)
        server.send(reply)
    }
}