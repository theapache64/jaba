package com.theapache64.jaba.cli

import com.theapache64.jaba.cli.models.Project
import com.theapache64.jaba.cli.utils.*
import java.io.File
import java.util.regex.Pattern

class Jaba2(
    private val project: Project,
    private val androidUtils: AndroidUtils
) {

    companion object {

        // app/build.gradle
        private val COMPILE_SDK_REGEX by lazy { Pattern.compile("compileSdkVersion (\\d+)") }
        private val MIN_SDK_REGEX by lazy { Pattern.compile("minSdkVersion (\\d+)") }
        private val TARGET_SDK_REGEX by lazy { Pattern.compile("targetSdkVersion (\\d+)") }
        private val ANDROIDX_APPCOMPAT_REGEX by lazy { Pattern.compile("implementation 'androidx\\.appcompat:appcompat:(.+)'") }
        private val KTX_VERSION_REGEX by lazy { Pattern.compile("implementation 'androidx\\.core:core-ktx:(.+)'") }
        private val CONSTRAINT_VERSION_REGEX by lazy { Pattern.compile("implementation 'androidx\\.constraintlayout:constraintlayout:(.+)'") }
        private val MATERIAL_VERSION_REGEX by lazy { Pattern.compile("implementation 'com\\.google\\.android\\.material:material:(.+)'") }
        private val JUNIT_VERSION_REGEX by lazy { Pattern.compile("testImplementation 'junit:junit:(.+)'") }
        private val RUNNER_VERSION_REGEX by lazy { Pattern.compile("androidTestImplementation 'androidx\\.test:runner:(.+)'") }
        private val ESPRESSO_VERSION_REGEX by lazy { Pattern.compile("androidTestImplementation 'androidx\\.test\\.espresso:espresso-core:(.+)'") }

        private val KOTLIN_VERSION_REGEX by lazy { Pattern.compile("ext.kotlin_version = '(.+)'") }
        private val GRADLE_VERSION_REGEX by lazy { Pattern.compile("classpath 'com.android.tools.build:gradle:(.+)'") }
    }


    fun build() {

        logDoing("Creating dirs...")
        createDirs()
        logDone()

        logDoing("Modifying app.gradle")
        doGradleThings()
        logDone()

        // Fix it xml first
        logDoing("Fixing XML style issue...")
        FixItXml.fixIt(project.dir)
        logDone()

        doAppThings()
        doManifestThings()
        doMainThings()

    }

    private fun doAppThings() {
        val appContent = AssetManager.getAppFile(project.packageName)
        androidUtils.appFile.writeText(appContent)
    }

    private fun doManifestThings() {
        androidUtils.manifestFile.delete()
        val newManifestContent = AssetManager.getManifestFile(project.packageName)
        androidUtils.manifestFile.writeText(newManifestContent)
    }

    private fun doMainThings() {

        // Creating MainViewModel
        val mainViewModelContent = AssetManager.getMainViewModel(project.packageName)
        androidUtils.mainViewModelFile.writeText(mainViewModelContent)

        // Delete default main activity
        androidUtils.oldMainActivityFile.delete()

        // Creating new one
        val mainContent = AssetManager.getMainActivity(project.packageName)
        androidUtils.mainActivityFile.writeText(mainContent)

        // Creating new layout file data binding
        val mainLayoutContent = AssetManager.getActivityMainLayout(project.packageName)
        androidUtils.mainLayoutFile.delete()
        androidUtils.mainLayoutFile.writeText(mainLayoutContent)

        // Creating content main
        val contentMainLayoutContent = AssetManager.getContentMainLayout(project.packageName)
        androidUtils.contentMainLayoutFile.delete()
        androidUtils.contentMainLayoutFile.writeText(contentMainLayoutContent)
    }

    /**
     * Creates structured directories
     */
    private fun createDirs() {

        val dirsToCreate = mutableListOf<String>(
            "data/local",
            "data/remote",
            "data/repositories",

            "di/components",
            "di/modules",

            "models",

            "ui/activities/main",
            "utils"
        )

        if (project.isNeedLogInScreen) {
            dirsToCreate.add("ui/activities/login")
        }

        if (project.isNeedSplashScreen) {
            dirsToCreate.add("ui/activities/splash")
        }

        // Creating dirs
        for (dir in dirsToCreate) {
            val fullDir = "${androidUtils.provideRootSourcePath()}/$dir"
            File(fullDir).mkdirs()
        }
    }

    /**
     * Updated app/build.gradle and project/build.gradle with deps and variable namings.
     */
    private fun doGradleThings() {

        // Getting latest version numbers
        val appGradleContent = androidUtils.gradleFile.readText()
        val regExParser = RegExParser(appGradleContent)

        val compileSdkVersion = regExParser.getFirst(COMPILE_SDK_REGEX)
        val minSdkVersion = regExParser.getFirst(MIN_SDK_REGEX)
        val targetSdkVersion = regExParser.getFirst(TARGET_SDK_REGEX)
        val appCompatVersion = regExParser.getFirst(ANDROIDX_APPCOMPAT_REGEX)
        val ktxVersion = regExParser.getFirst(KTX_VERSION_REGEX)
        val constraintVersion = regExParser.getFirst(CONSTRAINT_VERSION_REGEX)
        val materialVersion = regExParser.getFirst(MATERIAL_VERSION_REGEX)
        val jUnitVersion = regExParser.getFirst(JUNIT_VERSION_REGEX)
        val runnerVersion = regExParser.getFirst(RUNNER_VERSION_REGEX)
        val espressoVersion = regExParser.getFirst(ESPRESSO_VERSION_REGEX)

        // Delete default gradle file
        androidUtils.gradleFile.delete()

        // Get model file
        val newAppGradle =
            AssetManager.getAppBuildGradle(project.packageName)

        // Write
        androidUtils.gradleFile.writeText(newAppGradle)


        // Get kotlin version
        val projectGradleContent = androidUtils.projectGradleFile.readText()
        val projectRegExParse = RegExParser(projectGradleContent)
        val kotlinVersion = projectRegExParse.getFirst(KOTLIN_VERSION_REGEX)
        val gradleVersion = projectRegExParse.getFirst(GRADLE_VERSION_REGEX)

        // Delete default project
        androidUtils.projectGradleFile.delete()

        val newProjectGradle = AssetManager.getProjectBuildGradle(
            kotlinVersion!!,
            compileSdkVersion!!,
            minSdkVersion!!,
            targetSdkVersion!!,
            appCompatVersion!!,
            ktxVersion!!,
            constraintVersion!!,
            materialVersion!!,
            jUnitVersion!!,
            runnerVersion!!,
            espressoVersion!!,
            gradleVersion!!
        )

        androidUtils.projectGradleFile.writeText(newProjectGradle)
    }

}