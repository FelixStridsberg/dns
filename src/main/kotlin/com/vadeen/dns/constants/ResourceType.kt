package com.vadeen.dns.constants

sealed class ResourceType(type: Int) : DynamicEnum<Int>(type) {

    class A : ResourceType(1)
    class Unknown(type: Int) : ResourceType(type)

    companion object {
        fun of(type: Int): ResourceType =
            when (type) {
                1 -> A()
                else -> Unknown(type)
            }
    }
}
