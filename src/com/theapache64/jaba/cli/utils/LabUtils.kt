package com.theapache64.jaba.cli.utils

import java.io.File

object LabUtils {

    fun clean() {

        // Removing lab directory
        FileUtils.deleteDir("lab")
        val jabroidZipFile = File("assets/jabroid.zip")
        if (jabroidZipFile.exists()) {

            // Unzip jabroid.zip to lab
            FileUtils.unzip(jabroidZipFile, "lab")

        } else {
            println("jabroid.zip not found in ${jabroidZipFile.absolutePath}")
        }

    }

    fun cleanForPAS() {

        // Removing lab directory
        FileUtils.deleteDir("lab")
        val jabroidZipFile = File("assets/jabroid_with_some.zip")
        if (jabroidZipFile.exists()) {

            // Unzip jabroid.zip to lab
            FileUtils.unzip(jabroidZipFile, "lab")

        } else {
            println("jabroid.zip not found in ${jabroidZipFile.absolutePath}")
        }

    }
}