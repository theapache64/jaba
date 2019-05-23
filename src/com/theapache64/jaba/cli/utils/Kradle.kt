package com.theapache64.jaba.cli.utils

import java.io.File

object Kradle {

    fun getPackageName(gradleFile: File): String? {
        return getWithDoubleQuotes(gradleFile, "applicationId")
    }

    private fun getWithDoubleQuotes(gradleFile: File, delimiter: String) =
        gradleFile.readText().split("$delimiter \"")[1].split("\"")[0]

    fun getKotlinVersion(gradleFile: File): String {
        val readText = gradleFile.readText()
        return readText.split("ext.kotlin_version = '")[1].split("'")[0]
    }

    /**
     * To getWithDoubleQuotes compileSdkVersion, minSdkVersion and targetSdkVersion in a Triple object with respective position
     */
    fun getGradleVars(gradleFile: File): Triple<String, String, String> {
        val fileContent = gradleFile.readText()
        val compileSdkVersion = fileContent.split("compileSdkVersion ")[1].split("\n")[0]
        val minSdkVersion = fileContent.split("minSdkVersion ")[1].split("\n")[0]
        val targetSdkVersion = fileContent.split("targetSdkVersion ")[1].split("\n")[0]
        return Triple(compileSdkVersion, minSdkVersion, targetSdkVersion)
    }

}