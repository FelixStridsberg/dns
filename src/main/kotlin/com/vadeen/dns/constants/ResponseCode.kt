package com.vadeen.dns.constants

sealed class ResponseCode(code: Byte, private val str: String) : DynamicEnum<Byte>(code) {

    class NoError : ResponseCode(0, "NOERR")
    class Unknown(code: Byte) : ResponseCode(code, "U($code)")

    companion object {
        fun of(code: Byte): ResponseCode =
            when (code) {
                0.toByte() -> NoError()
                else -> Unknown(code)
            }
    }

    override fun toString(): String {
        return str
    }
}
