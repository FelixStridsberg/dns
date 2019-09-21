package com.vadeen.dns.message

import com.vadeen.dns.constants.ResourceClass
import com.vadeen.dns.constants.ResourceType

class UnknownResource(
    name: List<ByteArray>,
    resourceType: ResourceType,
    resourceClass: ResourceClass,
    ttl: Int,
    val data: ByteArray
) : Resource(name, resourceType, resourceClass, ttl)
