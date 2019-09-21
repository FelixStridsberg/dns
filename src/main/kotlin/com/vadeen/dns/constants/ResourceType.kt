package com.vadeen.dns.constants

sealed class ResourceType(type: Short) : DynamicEnum<Short>(type) {

    class Unknown(type: Short) : ResourceType(type)

    companion object {
        fun of(type: Short): ResourceType = Unknown(type)
    }
}
