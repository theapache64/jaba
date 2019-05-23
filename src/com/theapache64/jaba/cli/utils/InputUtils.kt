package com.theapache64.jaba.cli.utils

import java.lang.NumberFormatException
import java.util.*

class InputUtils private constructor(
    private val scanner: Scanner
) {

    /**
     * Get a String with given prompt as prompt
     */
    fun getString(prompt: String, isRequired: Boolean): String {
        print("$prompt: ")
        val value = scanner.nextLine()
        while (value.trim().isEmpty() && isRequired) {
            println("Invalid ${prompt.toLowerCase()} `$value`")
            return getString(prompt, isRequired)
        }
        return value
    }

    fun getInt(prompt: String, lowerBound: Int, upperBound: Int): Int {
        print("$prompt :")

        val sVal = scanner.nextLine()
        try {
            val value = sVal.toInt()
            if (value < lowerBound || value > upperBound) {
                println("Input must between $lowerBound and $upperBound")
                return getInt(prompt, lowerBound, upperBound)
            }
            return value
        } catch (e: NumberFormatException) {
            println("Invalid input `$sVal`")
            return getInt(prompt, lowerBound, upperBound)
        }
    }

    companion object {
        private var instance: InputUtils? = null
        fun getInstance(scanner: Scanner): InputUtils {
            if (instance == null) {
                instance = InputUtils(scanner)
            }
            return instance!!
        }
    }

}