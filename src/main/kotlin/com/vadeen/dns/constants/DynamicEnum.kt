package com.vadeen.dns.constants

/**
 * Since we must be able to handle operation and response codes that we have not implemented, we cannot use enums.
 *
 * Instead we can use sealed classes like so:
 *
 *  sealed class ResponseCode(code: Byte) : DynamicEnum(code) {
 *       KnownCode : ResponseCode(1),
 *       ...
 *       UnknownCode(code: Byte) : ResponseCode(code)
 *  }
 *
 * This way we can get the benefit of a enum like structure, but still the ability to handle unknown data, without
 * having to define the boiler plate every time.
 */
open class DynamicEnum(val value: Byte) {

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true

        if (other == null)
            return false

        if (other.javaClass != javaClass)
            return false

        other as DynamicEnum

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}