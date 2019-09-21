package com.vadeen.dns.constants

sealed class ResourceType(type: Int) : DynamicEnum<Int>(type) {

    class Unknown(type: Int) : ResourceType(type)

    companion object {
        fun of(type: Int): ResourceType = Unknown(type)
    }
}
