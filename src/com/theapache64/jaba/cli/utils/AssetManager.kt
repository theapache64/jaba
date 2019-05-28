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
        private const val KEY_APP_NAME = "\$APP_NAME"

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

        private val projectBuildGradleFile by lazy { File("assets/project.build.gradle") }
        val userIcon by lazy { File("assets/ic_user.png") }
        val androidIcon by lazy { File("assets/ic_android.png") }
        val logOutIcon by lazy { File("assets/ic_logout_white.xml") }
        val colorsFile by lazy { File("assets/colors.xml") }
    }

    /**
     * To return new app/build.gradle with given package name
     */
    fun getAppBuildGradle(): String {
        return withPackageNameReplacedFromAssets("app.build.gradle")
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


    private fun withPackageNameReplacedFromAssets(fileName: String): String {
        return getAssetContent(fileName).replace(KEY_PACKAGE_NAME, project.packageName)
    }

    private fun getAssetContent(fileName: String): String {
        return File("assets/$fileName").readText()
    }

    /**
     * To get MainActivity.kt
     */
    fun getMainActivity(): String {
        return withPackageNameReplacedFromAssets("MainActivity.kt")
    }

    /**
     * To get manifest file
     */
    fun getManifestFile(): String {
        return withPackageNameReplacedFromAssets("AndroidManifest.xml")
    }

    /**
     * To get App.kt
     */
    fun getAppFile(): String {
        return withPackageNameReplacedFromAssets("App.kt")
    }

    fun getMainViewModel(): String {
        return withPackageNameReplacedFromAssets("MainViewModel.kt")
    }

    fun getActivityMainLayout(): String {
        return withPackageNameReplacedFromAssets("activity_main.xml")
    }

    fun getContentMainLayout(): String {
        return withPackageNameReplacedFromAssets("content_main.xml")
    }

    fun getActivityBuilder(): String {
        return withPackageNameReplacedFromAssets("ActivitiesBuilderModule.kt")
    }

    fun getAppComponent(): String {
        return withPackageNameReplacedFromAssets("AppComponent.kt")
    }

    fun getUserRepository(): String {
        return withPackageNameReplacedFromAssets("UserPrefRepository.kt")
    }

    fun getLogInRequest(): String {
        return withPackageNameReplacedFromAssets("LogInRequest.kt")
    }

    fun getLogInResponse(): String {
        return withPackageNameReplacedFromAssets("LogInResponse.kt")
    }

    fun getAppModule(): String {
        return withPackageNameReplacedFromAssets("AppModule.kt")
    }

    fun getNetworkModule(): String {
        return withPackageNameReplacedFromAssets("NetworkModule.kt")
    }

    fun getViewModelModule(): String {
        return withPackageNameReplacedFromAssets("ViewModelModule.kt")
    }

    fun getApiInterface(): String {
        return withPackageNameReplacedFromAssets("ApiInterface.kt")
    }

    fun getSplashViewModel(): String {
        return withPackageNameReplacedFromAssets("SplashViewModel.kt")
    }

    fun getSplashActivity(): String {
        return withPackageNameReplacedFromAssets("SplashActivity.kt")
    }

    fun getStyles(): String {
        return withPackageNameReplacedFromAssets("styles.xml")
    }

    fun getLogInActivity(): String {
        return withPackageNameReplacedFromAssets("LogInActivity.kt")
    }

    fun getLogInViewModel(): String {
        return withPackageNameReplacedFromAssets("LogInViewModel.kt")
    }

    fun getLogInClickHandler(): String {
        return withPackageNameReplacedFromAssets("LogInClickHandler.kt")
    }

    fun getIds(): String {
        return getAssetContent("ids.xml")
    }

    fun getAuthRepository(): String {
        return withPackageNameReplacedFromAssets("AuthRepository.kt")
    }

    fun getSplashBg(): String {
        return getAssetContent("splash_bg.xml")
    }

    fun getAndroidVectorIcon(): String {
        return getAssetContent("ic_android_green_100dp.xml")
    }

    fun getLogInLayout(): String {
        return withPackageNameReplacedFromAssets("activity_log_in.xml")
    }

    fun getStringsXml(): String {
        return withAppNameReplacedFromAssets("strings.xml")
    }

    private fun withAppNameReplacedFromAssets(fileName: String): String {
        return getAssetContent(fileName).replace(KEY_APP_NAME, project.name)
    }

    fun getMenuMain(): String {
        return getAssetContent("menu_main.xml")
    }
}