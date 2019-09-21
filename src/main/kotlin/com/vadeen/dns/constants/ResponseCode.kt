package com.vadeen.dns.constants

sealed class ResponseCode(code: Byte) : DynamicEnum<Byte>(code) {

    class Unknown(code: Byte) : ResponseCode(code)

    companion object {
        fun of(code: Byte): ResponseCode = Unknown(code)
    }
}
