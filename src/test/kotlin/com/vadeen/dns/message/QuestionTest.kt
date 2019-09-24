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
}