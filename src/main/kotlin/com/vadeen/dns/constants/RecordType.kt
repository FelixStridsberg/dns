package com.vadeen.dns.constants

sealed class RecordType(type: Int) : DynamicEnum<Int>(type) {

    class A : RecordType(1)
    class Unknown(type: Int) : RecordType(type)

    companion object {
        fun of(type: Int): RecordType =
            when (type) {
                1 -> A()
                else -> Unknown(type)
            }
    }
}
