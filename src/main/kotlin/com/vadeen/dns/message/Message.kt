package com.vadeen.dns.message

import com.vadeen.dns.message.record.Record
import kotlin.text.StringBuilder

data class Message(
    val header: Header,
    val questions: List<Question>,
    val answerRecords: List<Record>,
    val authorityRecords: List<Record>,
    val additionalRecords: List<Record>
) {

    override fun toString(): String {
        val result = StringBuilder()
        result.append(header)

        if (questions.isNotEmpty()) {
            result.append("\n\nQuestions:\n")
                .append(questions.joinToString(separator = "\n"))
        }

        if (answerRecords.isNotEmpty()) {
            result.append("\n\nAnswers:\n")
                .append(answerRecords.joinToString(separator = "\n"))
        }

        if (authorityRecords.isNotEmpty()) {
            result.append("\n\nAuthorities:\n")
                .append(authorityRecords.joinToString(separator = "\n"))
        }

        if (additionalRecords.isNotEmpty()) {
            result.append("\n\nAdditional:\n")
                .append(additionalRecords.joinToString(separator = "\n"))
        }

        result.append('\n')
        return result.toString()
    }
}
