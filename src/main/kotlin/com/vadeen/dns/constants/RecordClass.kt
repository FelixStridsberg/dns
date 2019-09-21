package com.vadeen.dns.constants

sealed class RecordClass(cls: Int, private val str: String) : DynamicEnum<Int>(cls) {

    class IN : RecordClass(1, "IN")
    class Unknown(cls: Int) : RecordClass(cls, "U($cls)")

    companion object {
        fun of(cls: Int): RecordClass =
            when (cls) {
                1 -> IN()
                else -> Unknown(cls)
            }
    }

    override fun toString(): String {
        return str
    }
}
