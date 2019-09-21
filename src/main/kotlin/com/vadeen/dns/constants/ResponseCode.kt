package com.vadeen.dns.constants

sealed class ResponseCode(code: Byte) : DynamicEnum<Byte>(code) {

    class NoError : ResponseCode(0)
    class Unknown(code: Byte) : ResponseCode(code)

    companion object {
        fun of(code: Byte): ResponseCode =
            when (code) {
                0.toByte() -> NoError()
                else -> Unknown(code)
            }
    }
}
