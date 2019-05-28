package com.theapache64.jaba.cli

import com.theapache64.jaba.cli.models.Project
import com.theapache64.jaba.cli.utils.*
import java.io.File
import java.util.regex.Pattern

class Jaba(
    private val project: Project,
    private val androidUtils: AndroidUtils
) {

    private val assetManager = AssetManager(project)

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

        // Create app file
        createFile(
            assetManager.getAppFile(),
            androidUtils.appFile
        )
    }

    private fun doManifestThings() {

        // Creating manifest file
        createFile(
            assetManager.getManifestFile(),
            androidUtils.manifestFile
        )
    }

    private fun doMainThings() {

        // Creating MainViewModel
        createFile(
            assetManager.getMainViewModel(),
            androidUtils.mainViewModelFile
        )

        // Delete default main activity
        androidUtils.oldMainActivityFile.delete()

        // Creating new one
        createFile(
            assetManager.getMainActivity(),
            androidUtils.mainActivityFile
        )

        // Creating new layout file data binding
        createFile(
            assetManager.getActivityMainLayout(),
            androidUtils.mainLayoutFile
        )

        // Creating content main
        createFile(
            assetManager.getContentMainLayout(),
            androidUtils.contentMainLayoutFile
        )

        // Create activity builder
        createFile(
            assetManager.getActivityBuilder(),
            androidUtils.activityBuilderModuleFile
        )

        // Creating dagger component
        createFile(
            assetManager.getAppComponent(),
            androidUtils.appComponentFile
        )

        // Create login request file
        createFile(
            assetManager.getLogInRequest(),
            androidUtils.logInRequestFile
        )

        // Creating login response
        createFile(
            assetManager.getLogInResponse(),
            androidUtils.logInResponseFile
        )

        // Create user pref repositories
        createFile(
            assetManager.getUserRepository(),
            androidUtils.userRepoFile
        )

        // Create AppModule
        createFile(
            assetManager.getAppModule(),
            androidUtils.appModuleFile
        )

        // Create NetworkModule
        createFile(
            assetManager.getNetworkModule(),
            androidUtils.networkModuleFile
        )

        // Create viewModelModule
        createFile(
            assetManager.getViewModelModule(),
            androidUtils.viewModelModuleFile
        )

        // Create ApiInterface
        createFile(
            assetManager.getApiInterface(),
            androidUtils.apiInterfaceFile
        )

        // Create splash view model
        createFile(
            assetManager.getSplashViewModel(),
            androidUtils.splashViewModelFile
        )

        // Create splash activity
        createFile(
            assetManager.getSplashActivity(),
            androidUtils.splashActivityFile
        )

        // Replace with styles
        createFile(
            assetManager.getStyles(),
            androidUtils.stylesFile
        )

        // Create ids
        createFile(
            assetManager.getIds(),
            androidUtils.idsFile
        )

        // Create login activity
        createFile(
            assetManager.getLogInActivity(),
            androidUtils.logInActivityFile
        )

        // Create login view model
        createFile(
            assetManager.getLogInViewModel(),
            androidUtils.logInViewModelFile
        )

        // Create login click handler
        createFile(
            assetManager.getLogInClickHandler(),
            androidUtils.loginClickHandler
        )

        // Create auth repository
        createFile(
            assetManager.getAuthRepository(),
            androidUtils.authRepositoryFile
        )

        // Create splash bg
        createFile(
            assetManager.getSplashBg(),
            androidUtils.splashBgFile
        )

        // Create vector icon

        createFile(
            assetManager.getLogInLayout(),
            androidUtils.logInLayoutFile
        )

        AssetManager.userIcon.copyTo(androidUtils.userIconFile)
        AssetManager.androidIcon.copyTo(androidUtils.androidIcon)
        AssetManager.logOutIcon.copyTo(androidUtils.logOutIcon)

        // Create string xml
        createFile(
            assetManager.getStringsXml(),
            androidUtils.stringXmlFile
        )

        // Create menu main
        createFile(
            assetManager.getMenuMain(),
            androidUtils.menuMainFile
        )

        // Create colors
        AssetManager.colorsFile.copyTo(androidUtils.colorsFile, true)
    }

    private fun createFile(fileContent: String, file: File) {

        // Deleting old file
        file.delete()

        // Deleting parent dirs
        file.parentFile.mkdirs()

        // Writing to file
        file.writeText(fileContent)
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
            assetManager.getAppBuildGradle()

        // Write
        androidUtils.gradleFile.writeText(newAppGradle)


        // Get kotlin version
        val projectGradleContent = androidUtils.projectGradleFile.readText()
        val projectRegExParse = RegExParser(projectGradleContent)
        val kotlinVersion = projectRegExParse.getFirst(KOTLIN_VERSION_REGEX)
        val gradleVersion = projectRegExParse.getFirst(GRADLE_VERSION_REGEX)

        // Delete default project
        androidUtils.projectGradleFile.delete()

        val newProjectGradle = assetManager.getProjectBuildGradle(
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