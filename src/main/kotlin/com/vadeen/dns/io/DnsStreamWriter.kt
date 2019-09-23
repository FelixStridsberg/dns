package com.vadeen.dns.io

import java.io.OutputStream

class DnsStreamWriter(private val stream: OutputStream) {

    fun writeByte(byte: Byte) = stream.write(byte.toInt())

    fun writeShort(value: Int) {
        stream.write(value shr 8)
        stream.write(value)
    }

    fun writeInt(value: Int) {
        writeShort(value shr 16)
        writeShort(value)
    }

    fun writeLabels(labels: List<ByteArray>) {
        for (label in labels) {
            stream.write(label.size)
            stream.write(label)
        }
        stream.write(0x00)
    }

    fun writeBytes(data: ByteArray) {
        stream.write(data)
    }
}