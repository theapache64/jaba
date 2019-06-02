package com.theapache64.jaba.cli

import com.theapache64.jaba.cli.models.Architectures
import com.theapache64.jaba.cli.models.Project
import com.theapache64.jaba.cli.utils.*
import java.io.FileNotFoundException
import java.util.*

const val IS_DEBUG = false
const val ERROR_NOT_AN_ANDROID_PROJECT = "ERROR_NOT_AN_ANDROID_PROJECT"
const val ERROR_UNSUPPORTED_ARCH = "UNSUPPORTED_ARCH"
const val ERROR_NOT_KOTLIN_PROJECT = "NOT_KOTLIN_PROJECT"
const val JABA_API_BASE_URL = "http://theapache64.com/mock_api/get_json/jaba/"

/**
 * Magic starts from here
 */
fun main() {

    if (IS_DEBUG) {
        logDoing("Cleaning lab...")
        LabUtils.clean()
        logDone()
    }


    // Current directory will be treated as an android project
    val currentDir = if (IS_DEBUG) "lab/jabroid" else System.getProperty("user.dir")
    //val currentDir = "lab/jabroid"
    //val currentDir = "/home/theapache64/Documents/projects/MyApp"
    try {
        val androidUtils = AndroidUtils(currentDir)

        if (androidUtils.isAndroidProject()) {

            // Getting project name
            val projectName = androidUtils.provideProjectName()
            val packageName = androidUtils.providePackageName()

            if (androidUtils.isKotlinProject()) {

                println("Project : $projectName")
                println("Package : $packageName")
                println()
                println("Choose architecture")
                for (arch in Architectures.values().withIndex()) {
                    println("${arch.index + 1}) ${arch.value}")
                }

                val scanner = Scanner(System.`in`)
                val inputUtils = InputUtils.getInstance(scanner)

                // Asking architecture
                val totalArchs = Architectures.values().size
                val architecture = if (IS_DEBUG) 1 else inputUtils.getInt("Response", 1, totalArchs)
                val selArch = Architectures.values().get(architecture - 1)

                if (selArch == Architectures.MVP) {
                    failure(ERROR_UNSUPPORTED_ARCH, "MVP not supported yet!")
                    return
                }

                // Google Fonts
                val googleFontResponse =
                    if (IS_DEBUG) "yes" else inputUtils.getString("Do you need google fonts? (y/N)", true)
                val isNeedGoogleFont = isYes(googleFontResponse)

                // Network
                val networkResponse =
                    if (IS_DEBUG) "yes" else inputUtils.getString("Do you need network module ? (y/N)", true)
                val isNeedNetwork = isYes(networkResponse)

                var baseUrl: String? = null
                if (isNeedNetwork) {
                    baseUrl = if (IS_DEBUG) "http://myapi.com/" else inputUtils.getString(
                        "Enter base url : (empty to use default jaba api)",
                        false
                    )

                    if (baseUrl.trim().isEmpty()) {
                        baseUrl = JABA_API_BASE_URL
                    }
                }

                // Splash
                val splashResponse =
                    if (IS_DEBUG) "yes" else inputUtils.getString("Do you need splash screen? (y/N)", true)
                val isNeedSplashScreen = isYes(splashResponse)


                val isNeedLogInScreen = if (isNeedNetwork) {
                    val logInResponse =
                        if (IS_DEBUG) "yes" else inputUtils.getString("Do you need login screen? (y/N)", true)
                    isYes(logInResponse)
                } else {
                    false
                }


                val project = Project(
                    projectName,
                    currentDir,
                    packageName,
                    architecture,
                    isNeedGoogleFont,
                    isNeedNetwork,
                    baseUrl,
                    isNeedSplashScreen,
                    isNeedLogInScreen
                )


                Jaba(project, androidUtils).build()
                // Jaba(androidUtils, project).buildOld()

            } else {
                failure(ERROR_NOT_KOTLIN_PROJECT, "$currentDir is not a kotlin android project")
            }

        } else {
            notAnAndroidProject(currentDir)
        }
    } catch (e: FileNotFoundException) {
        notAnAndroidProject(currentDir)
    }
}

fun notAnAndroidProject(currentDir: String) {
    // invalid android project
    failure(ERROR_NOT_AN_ANDROID_PROJECT, "$currentDir is not an android project")
}


fun isYes(response: String): Boolean = response.trim().toLowerCase().let {
    val isYes = it == "y" || it == "yes"
    println("Response : ${if (isYes) "yes" else "no"}")
    return@let isYes
}

fun failure(errorCode: String, message: String) {
    println("ERROR : $errorCode : $message")
}

