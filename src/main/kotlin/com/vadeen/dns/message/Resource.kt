package com.vadeen.dns.message

import com.vadeen.dns.constants.ResourceClass
import com.vadeen.dns.constants.ResourceType

abstract class Resource(
    val name: List<ByteArray>,
    val resourceType: ResourceType,
    val resourceClass: ResourceClass,
    val ttl: Int
)
