package com.vadeen.dns.constants

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class DynamicEnumTest {

    @Test
    internal fun testUnknown() {
        val en = DynamicEnum(0xFF.toByte())
        assertEquals(0xFF.toByte(), en.value)
    }

    @Test
    internal fun testHash() {
        val en = DynamicEnum(0xFF.toByte())
        assertEquals(0xFF.toByte().hashCode(), en.hashCode())
    }

    @Test
    internal fun testHashNull() {
        val en = DynamicEnum(null)
        assertEquals(0, en.hashCode())
    }

    @Test
    internal fun testEquals() {
        val en1 = DynamicEnum(0XFF.toByte())
        val en2 = DynamicEnum(0XFF.toByte())
        val en3 = DynamicEnum(0X00.toByte())

        assertTrue(en1 == en1)
        assertTrue(en1 == en2)
        assertFalse(en1 == en3)
        assertFalse(en1.equals(1))
        assertFalse(en1.equals(null))
    }

    @Test
    internal fun testNulEqualsNull() {
        val en1 = DynamicEnum(null)
        val en2 = DynamicEnum(null)
        val en3 = DynamicEnum(1)

        assertTrue(en1.equals(en2))
        assertFalse(en1.equals(en3))
    }
}