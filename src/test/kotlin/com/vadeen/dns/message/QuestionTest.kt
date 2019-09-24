package com.vadeen.dns.message

import com.vadeen.dns.constants.RecordClass
import com.vadeen.dns.constants.RecordType
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class QuestionTest {

    @Test
    fun testToString() {
        val name = DomainName.of("ns.vadeen.com")
        val question = Question(name, RecordType.A(), RecordClass.IN())

        assertEquals("ns.vadeen.com                        IN         A", question.toString())
    }

    @Test
    internal fun testHash() {
        val question1 = Question(DomainName.of("ns.com"), RecordType.A(), RecordClass.IN())
        val question2 = Question(DomainName.of("ns.com"), RecordType.A(), RecordClass.IN())
        val question3 = Question(DomainName.of("ns.com"), RecordType.A(), RecordClass.Unknown(99))

        assertEquals(question1.hashCode(), question2.hashCode())
        assertNotEquals(question1.hashCode(), question3.hashCode())
    }

    @Test
    internal fun testEquals() {
        val question1 = Question(DomainName.of("ns.com"), RecordType.A(), RecordClass.IN())
        val question2 = Question(DomainName.of("ns.com"), RecordType.A(), RecordClass.IN())
        val question3 = Question(DomainName.of("ns.com"), RecordType.A(), RecordClass.Unknown(99))

        assertTrue(question1 == question1)
        assertTrue(question1 == question2)
        assertFalse(question1 == question3)
        assertFalse(question1.equals(1))
        assertFalse(question1.equals(null))
    }
}