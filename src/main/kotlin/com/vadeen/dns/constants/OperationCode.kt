package com.vadeen.dns.constants

sealed class OperationCode(code: Byte) : DynamicEnum(code) {

    class Unknown(code: Byte) : OperationCode(code)

    companion object {
        fun of(code: Byte): OperationCode = Unknown(code)
    }
}
