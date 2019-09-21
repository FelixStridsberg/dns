package com.vadeen.dns.constants

sealed class ResourceClass(cls: Int) : DynamicEnum<Int>(cls) {

    class IN : ResourceClass(1)
    class Unknown(cls: Int) : ResourceClass(cls)

    companion object {
        fun of(cls: Int): ResourceClass =
            when (cls) {
                1 -> IN()
                else -> Unknown(cls)
            }
    }
}
