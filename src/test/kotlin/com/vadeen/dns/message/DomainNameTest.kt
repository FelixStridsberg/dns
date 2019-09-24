package com.vadeen.dns.message

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class DomainNameTest {
    @Test
    internal fun testHash() {
        val labels = arrayOf("label".toByteArray())
        val domain = DomainName(labels)
        assertEquals(labels.contentDeepHashCode(), domain.hashCode())
    }

    @Test
    internal fun testEquals() {
        val domain1 = DomainName.of("ns.vadeen.com")
        val domain2 = DomainName.of("ns.vadeen.com")
        val domain3 = DomainName.of("com.vadeen.ns")

        assertTrue(domain1 == domain1)
        assertTrue(domain1 == domain2)
        assertFalse(domain1 == domain3)
        assertFalse(domain1.equals(1))
        assertFalse(domain1.equals(null))
    }
}