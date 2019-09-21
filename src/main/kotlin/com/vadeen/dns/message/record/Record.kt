package com.vadeen.dns.message.record

import com.vadeen.dns.constants.ResourceClass
import com.vadeen.dns.constants.ResourceType

abstract class Record(
    val name: List<ByteArray>,
    val resourceType: ResourceType,
    val resourceClass: ResourceClass,
    val ttl: Int
)
