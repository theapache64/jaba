package com.theapache64.jaba.cli.utils

fun logDoing(message: String) {
    println("⏳ $message")
}

fun logDone(message: String) {
    println("✔️ $message")
}

fun logDone() {
    logDone("Done")
}