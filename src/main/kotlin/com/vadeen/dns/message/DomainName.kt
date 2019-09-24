package com.vadeen.dns.message

data class DomainName(val labels: List<ByteArray>) {

    companion object {
        fun of(str: String) = DomainName(str.split(".").map { it.toByteArray() })
    }

    override fun toString() = labels.joinToString(separator = ".") { String(it) }

}
