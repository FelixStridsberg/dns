package com.vadeen.dns.io

import java.io.InputStream

/**
 * A reader that can read all data types in a dns input stream.
 */
class DnsStreamReader(private val stream: InputStream) {

    fun readByte() = readRawByte().toByte()

    fun readShort() = ((readRawByte() shl 8) or readRawByte()).toShort()

    fun readDomainName(): List<ByteArray> {
        val labels = mutableListOf<ByteArray>()
        while (true) {
            val label = readLabel() ?: break
            labels.add(label)
        }
        return labels
    }

    fun readRawByte(): Int {
        val byte = stream.read()
        if (byte == -1)
            throw DnsIOException("Unexpected end of stream.")
        return byte
    }

    private fun readLabel(): ByteArray? {
        val length = readRawByte()
        if (length == 0)
            return null

        val data = stream.readNBytes(length)
        if (data.size < length)
            throw DnsIOException("Unexpected end of stream.")

        return data
    }
}