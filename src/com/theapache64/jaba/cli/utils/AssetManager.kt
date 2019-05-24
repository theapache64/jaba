package com.theapache64.jaba.cli.utils

import java.io.File

/**
 * To get files from assets directory
 */
object AssetManager {

    // app/build.gradle
    private const val KEY_PACKAGE_NAME = "\$PACKAGE_NAME"

    // project/build.gradle
    private const val KEY_KOTLIN_VERSION = "\$KOTLIN_VERSION"
    private const val KEY_COMPILE_SDK_VERSION = "\$COMPILE_SDK_VERSION"
    private const val KEY_MIN_SDK_VERSION = "\$MIN_SDK_VERSION"
    private const val KEY_TARGET_SDK_VERSION = "\$TARGET_SDK_VERSION"
    private const val KEY_APPCOMPAT_VERSION = "\$APPCOMPAT_VERSION"
    private const val KEY_KTX_VERSION = "\$KTX_VERSION"
    private const val KEY_CONSTRAINT_VERSION = "\$CONSTRAINT_VERSION"
    private const val KEY_MATERIAL_VERSION = "\$MATERIAL_VERSION"
    private const val KEY_JUNIT_VERSION = "\$JUNIT_VERSION"
    private const val KEY_RUNNER_VERSION = "\$RUNNER_VERSION"
    private const val KEY_ESPRESSO_VERSION = "\$ESPRESSO_VERSION"
    private const val KEY_GRADLE_VERSION = "\$GRADLE_VERSION"


    private val appBuildGradleFile by lazy { File("assets/app.build.gradle") }
    private val projectBuildGradleFile by lazy { File("assets/project.build.gradle") }
    private val mainActivityFile by lazy { File("assets/MainActivity.kt") }
    private val manifestFile by lazy { File("assets/AndroidManifest.xml") }
    private val appFile by lazy { File("assets/App.kt") }
    private val mainViewModelFile by lazy { File("assets/MainViewModel.kt") }
    private val mainLayoutFile by lazy { File("assets/activity_main.xml") }
    private val activityBuilderModuleFile by lazy { File("assets/ActivitiesBuilderModule.kt") }

    /**
     * To return new app/build.gradle with given package name
     */
    fun getAppBuildGradle(packageName: String): String {
        return appBuildGradleFile.readText()
            .replace(KEY_PACKAGE_NAME, packageName)
    }


    /**
     * To get project build gradle with given params
     */
    fun getProjectBuildGradle(
        kotlinVersion: String,
        compileSdkVersion: String,
        minSdkVersion: String,
        targetSdkVersion: String,
        appCompatVersion: String,
        ktxVersion: String,
        constraintVersion: String,
        materialVersion: String,
        jUnitVersion: String,
        runnerVersion: String,
        espressoVersion: String,
        gradleVersion: String
    ): String {

        return projectBuildGradleFile.readText()
            .replace(KEY_KOTLIN_VERSION, kotlinVersion)
            .replace(KEY_COMPILE_SDK_VERSION, compileSdkVersion)
            .replace(KEY_MIN_SDK_VERSION, minSdkVersion)
            .replace(KEY_TARGET_SDK_VERSION, targetSdkVersion)
            .replace(KEY_APPCOMPAT_VERSION, appCompatVersion)
            .replace(KEY_KTX_VERSION, ktxVersion)
            .replace(KEY_CONSTRAINT_VERSION, constraintVersion)
            .replace(KEY_MATERIAL_VERSION, materialVersion)
            .replace(KEY_JUNIT_VERSION, jUnitVersion)
            .replace(KEY_RUNNER_VERSION, runnerVersion)
            .replace(KEY_ESPRESSO_VERSION, espressoVersion)
            .replace(KEY_GRADLE_VERSION, gradleVersion)
    }

    /**
     * To get MainActivity.kt
     */
    fun getMainActivity(packageName: String): String {
        return mainActivityFile.readText()
            .replace(KEY_PACKAGE_NAME, packageName)
    }

    /**
     * To get manifest file
     */
    fun getManifestFile(packageName: String): String {
        return manifestFile.readText()
            .replace(KEY_PACKAGE_NAME, packageName)
    }

    /**
     * To get App.kt
     */
    fun getAppFile(packageName: String): String {
        return appFile.readText()
            .replace(KEY_PACKAGE_NAME, packageName)
    }

    fun getMainViewModel(packageName: String): String {
        return mainViewModelFile.readText()
            .replace(KEY_PACKAGE_NAME, packageName)
    }

    fun getActivityMainLayout(packageName: String): String {
        return mainLayoutFile.readText()
            .replace(KEY_PACKAGE_NAME, packageName)
    }

}