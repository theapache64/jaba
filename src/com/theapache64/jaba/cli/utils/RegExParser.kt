package com.theapache64.jaba.cli.utils

import java.util.regex.Pattern

class RegExParser(private val content: String) {

    fun getFirst(pattern: Pattern): String? {
        val matcher = pattern.matcher(content)
        if (matcher.find()) {
            return matcher.group(1)
        }
        return null
    }

}