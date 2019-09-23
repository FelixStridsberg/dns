package com.vadeen.dns.constants

sealed class OperationCode(code: Byte, private val str: String) : DynamicEnum<Byte>(code) {

    class Query : OperationCode(0, "QUERY")
    class Unknown(code: Byte) : OperationCode(code, "U($code)")

    companion object {
        fun of(code: Byte): OperationCode = Unknown(code)
    }

    override fun toString(): String {
        return str
    }
}
