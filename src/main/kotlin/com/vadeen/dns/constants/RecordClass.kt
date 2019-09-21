package com.vadeen.dns.constants

sealed class RecordClass(cls: Int) : DynamicEnum<Int>(cls) {

    class IN : RecordClass(1)
    class Unknown(cls: Int) : RecordClass(cls)

    companion object {
        fun of(cls: Int): RecordClass =
            when (cls) {
                1 -> IN()
                else -> Unknown(cls)
            }
    }
}
