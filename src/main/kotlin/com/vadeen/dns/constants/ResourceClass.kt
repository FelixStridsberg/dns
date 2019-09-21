package com.vadeen.dns.constants

sealed class ResourceClass(cls: Int) : DynamicEnum<Int>(cls) {

    class Unknown(cls: Int) : ResourceClass(cls)

    companion object {
        fun of(cls: Int): ResourceClass = Unknown(cls)
    }
}
