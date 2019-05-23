package com.theapache64.jaba.cli.exceptions

class JabaException(
    private val errorCode: String,
    message: String
) : RuntimeException(message) {


}