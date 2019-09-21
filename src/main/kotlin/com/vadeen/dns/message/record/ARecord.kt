package com.vadeen.dns.message.record

import com.vadeen.dns.constants.ResourceClass
import com.vadeen.dns.constants.ResourceType

class ARecord(
    name: List<ByteArray>,
    resourceType: ResourceType,
    resourceClass: ResourceClass,
    ttl: Int,
    val ip: ByteArray
) : Record(name, resourceType, resourceClass, ttl) {

    init {
        require(ip.size == 4) { "A Record data size must be 4" }
    }

    companion object {
        fun of(name: List<ByteArray>, rtype: ResourceType, rclass: ResourceClass, ttl: Int, data: ByteArray) =
            ARecord(name, rtype, rclass, ttl, data)
    }

}