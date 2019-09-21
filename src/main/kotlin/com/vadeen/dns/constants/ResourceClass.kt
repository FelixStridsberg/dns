package com.vadeen.dns.constants

sealed class ResourceClass(cls: Short) : DynamicEnum<Short>(cls) {

    class Unknown(cls: Short) : ResourceClass(cls)

    companion object {
        fun of(cls: Short): ResourceClass = Unknown(cls)
    }
}
