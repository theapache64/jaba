package com.theapache64.jaba.cli.utils

object StringUtils {

    private val camelCaseRegEx by lazy { Regex("([a-z])([A-Z]+)") }

    fun camelCaseToSnackCase(camelCaseString: String): String {
        return camelCaseString.replace(camelCaseRegEx, "\$1_\$2")
            .toLowerCase()
    }
}