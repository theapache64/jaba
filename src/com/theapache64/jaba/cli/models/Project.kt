package com.theapache64.jaba.cli.models

import java.io.File

data class Project(
    val name: String,
    val dir: String,
    val packageName: String,
    val architecture: Int,
    val isNeedGoogleFontsModule: Boolean,
    val isNeedNetworkModule: Boolean,
    val baseUrl: String?,
    val isNeedSplashScreen: Boolean,
    val isNeedLogInScreen: Boolean
) {

    val rootSrcPath: String = "$dir/app/src/main/java/${packageName.replace('.', '/')}"
    val mainLayoutFile = File("$dir/app/src/main/res/layout/activity_main.xml")
    val contentMainLayoutFile = File("$dir/app/src/main/res/layout/content_main.xml")
    val gradleFile: File = File("$dir/app/build.gradle")
    val projectGradleFile = File("$dir/build.gradle")
    val manifestFile = File("$dir/app/src/main/AndroidManifest.xml")

    companion object {
        const val ARCH_MVVP = 1
        const val ARCH_MVP = 2
    }
}