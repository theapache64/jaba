package com.theapache64.jaba.cli.utils

import com.theapache64.jaba.cli.models.Project
import java.io.File

/**
 * To get files from assets directory
 */
class AssetManager(
    private val project: Project
) {

    companion object {

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
        private val contentMainLayout by lazy { File("assets/content_main.xml") }
        private val activityBuilderModuleFile by lazy { File("assets/ActivitiesBuilderModule.kt") }
        private val appComponentFile by lazy { File("assets/AppComponent.kt") }
        private val userRepositoryFile by lazy { File("assets/UserPrefRepository.kt") }
        private val logInRequestFile by lazy { File("assets/LogInRequest.kt") }
        private val logInResponseFile by lazy { File("assets/LogInResponse.kt") }
        private val appModuleFile by lazy { File("assets/AppModule.kt") }
    }

    /**
     * To return new app/build.gradle with given package name
     */
    fun getAppBuildGradle(): String {
        return withPackageNameReplaced(appBuildGradleFile)
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
    fun getMainActivity(): String {
        return withPackageNameReplaced(mainActivityFile)
    }

    private fun withPackageNameReplaced(file: File): String {
        return withPackageNameReplaced(file)
    }

    /**
     * To get manifest file
     */
    fun getManifestFile(): String {
        return withPackageNameReplaced(manifestFile)
    }

    /**
     * To get App.kt
     */
    fun getAppFile(): String {
        return withPackageNameReplaced(appFile)
    }

    fun getMainViewModel(): String {
        return withPackageNameReplaced(mainViewModelFile)
    }

    fun getActivityMainLayout(): String {
        return withPackageNameReplaced(mainLayoutFile)
    }

    fun getContentMainLayout(): String {
        return withPackageNameReplaced(contentMainLayout)
    }

    fun getActivityBuilder(): String {
        return withPackageNameReplaced(activityBuilderModuleFile)
    }

    fun getAppComponent(): String {
        return withPackageNameReplaced(appComponentFile)
    }

    fun getUseRepository(): String {
        return withPackageNameReplaced(userRepositoryFile)
    }

    fun getLogInRequest(): String {
        return withPackageNameReplaced(logInRequestFile)
    }

    fun getLogInResponse(): String {
        return withPackageNameReplaced(logInResponseFile)
    }

    fun getAppModule(): String {
        return withPackageNameReplaced(appModuleFile)
    }


}