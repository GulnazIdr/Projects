package com.example.englishreviser.ui_helpers

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 10) text.text.substring(0..9) else text.text

        var out = if (trimmed.isNotEmpty()) "7(" else ""

        for (i in trimmed.indices) {
            if (i == 3) out += ") "
            if (i == 6 || i == 8) out += "-"
            out += trimmed[i]
        }
        return TransformedText(
            androidx.compose.ui.text.AnnotatedString(out),
            phoneNumberOffsetTranslator
        )
    }

    private val phoneNumberOffsetTranslator = object : OffsetMapping {

        override fun originalToTransformed(offset: Int): Int =
            when (offset) {
                0 -> offset
                in 1..3 -> offset + 2
                in 4..6 -> offset + 4
                in 7..8 -> offset + 5
                else -> offset + 6
            }

        override fun transformedToOriginal(offset: Int): Int =
            when (offset) {
                in 0..2 -> 0
                in 3..5 -> offset-2
                in 6..10 -> offset-4
                in 11..13 -> offset-5
                else -> offset-6
            }
    }
}