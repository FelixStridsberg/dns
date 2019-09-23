package com.vadeen.dns.constants

sealed class RecordType(type: Int, private val str: String) : DynamicEnum<Int>(type) {

    class A : RecordType(1, "A")
    class Unknown(type: Int) : RecordType(type, "U($type)")

    companion object {
        fun of(type: Int): RecordType =
            when (type) {
                1 -> A()
                else -> Unknown(type)
            }
    }

    override fun toString(): String {
        return str
    }
}
