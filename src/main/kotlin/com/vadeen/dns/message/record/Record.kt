package com.vadeen.dns.message.record

import com.vadeen.dns.constants.ResourceClass
import com.vadeen.dns.constants.ResourceType
import com.vadeen.dns.message.UnknownResource

abstract class Record(
    val name: List<ByteArray>,
    val resourceType: ResourceType,
    val resourceClass: ResourceClass,
    val ttl: Int
) {
    companion object {
        fun of(name: List<ByteArray>, rtype: ResourceType, rclass: ResourceClass, ttl: Int, data: ByteArray) : Record =
            when (rtype) {
                is ResourceType.Unknown -> UnknownResource.of(name, rtype, rclass, ttl, data)
                is ResourceType.A -> ARecord.of(name, rtype, rclass, ttl, data)
            }
    }
}
