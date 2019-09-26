package com.vadeen.dns.io

import com.vadeen.dns.exception.DnsIOException
import com.vadeen.dns.exception.DnsParseException
import com.vadeen.dns.exception.NotImplementedException
import java.io.ByteArrayInputStream
import java.io.InputStream

/**
 * A reader that can read all data types in a dns input stream.
 */
class DnsStreamReader private constructor(private val data: ByteArray, private val stream: InputStream) {

    companion object {
        fun of(data: ByteArray): DnsStreamReader = DnsStreamReader(data, ByteArrayInputStream(data))
    }

    fun readByte() = readRawByte().toByte()

    fun readShort() = (readRawByte() shl 8) or readRawByte()

    fun readInt() = (readShort() shl 16) or readShort()

    fun readBytes(n: Int) = readBytes(stream, n)

    fun readDomainName() = readDomainName(stream)

    fun readRawByte() = readRawByte(stream)

    /**
     * The domain names is either represented as:
     * - A sequence of labels.
     * - A pointer to a sequence of labels.
     * - A sequence of labels ending with a pointer.
     *
     * If the offset is 0, that's the end.
     * If the offset starts with 0xC, then it's a pointer combined with the next byte.
     * Otherwise it is a length of the upcoming label.
     *
     * Ref: https://tools.ietf.org/html/rfc1035#section-4.1.4
     */
    private fun readDomainName(stream: InputStream, recursion: Int = 0): Array<ByteArray> {
        if (recursion > 1)
            throw DnsParseException("Recursive label pointers not allowed.")

        val result = mutableListOf<ByteArray>()
        while (true) {
            val length = readRawByte(stream)
            if (length == 0) // End of labels
                break

            // Pointer
            if (length and 0xC0 == 0xC0) {
                val offset = ((length and 0x3F) shl 8) or readRawByte(stream)
                val offsetStream = ByteArrayInputStream(data, offset, 1000)
                val labels = readDomainName(offsetStream, recursion + 1)
                result.addAll(labels)
                return result.toTypedArray()
            }

            // 0x80 and 0x40 is reserved for future use.
            if (length and 0xC0 != 0) {
                throw NotImplementedException("Invalid label type.")
            }

            // Simple label, just read it.
            result.add(readBytes(stream, length))
        }

        return result.toTypedArray()
    }

    private fun readBytes(stream: InputStream, n: Int): ByteArray {
        val data = stream.readNBytes(n)
        if (data.size < n)
            throw DnsIOException("Unexpected end of stream.")

        return data
    }

    private fun readRawByte(stream: InputStream): Int {
        val byte = stream.read()
        if (byte == -1)
            throw DnsIOException("Unexpected end of stream.")
        return byte
    }
}