package com.vadeen.dns.message

data class DomainName(val labels: Array<ByteArray>) {

    companion object {
        fun of(str: String) = DomainName(str.split(".").map { it.toByteArray() }.toTypedArray())
    }

    override fun toString() = labels.joinToString(separator = ".") { String(it) }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true

        if (other == null)
            return false

        if (other.javaClass != javaClass)
            return false

        other as DomainName

        return labels.contentDeepEquals(other.labels)
    }

    override fun hashCode(): Int {
        return labels.contentDeepHashCode()
    }
}
