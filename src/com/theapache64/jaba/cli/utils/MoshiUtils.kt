package com.theapache64.jaba.cli.utils

import com.squareup.moshi.Moshi
import com.theapache64.jaba.cli.models.Project

object MoshiUtils {
    private val moshi by lazy {
        Moshi.Builder()
            .build()
    }

    val projectAdapter by lazy {
        moshi.adapter(Project::class.java)
    }

}