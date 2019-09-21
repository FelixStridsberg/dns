package com.vadeen.dns.message

import com.vadeen.dns.constants.ResourceClass
import com.vadeen.dns.constants.ResourceType
import com.vadeen.dns.message.record.Record

class UnknownResource(
    name: List<ByteArray>,
    resourceType: ResourceType,
    resourceClass: ResourceClass,
    ttl: Int,
    val data: ByteArray
) : Record(name, resourceType, resourceClass, ttl)
