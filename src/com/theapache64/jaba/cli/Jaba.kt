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

        // Create app file
        logDoing("Creating App.kt ...")
        createFile(
            assetManager.getAppFile(),
            androidUtils.appFile
        )
        logDone()

        // Creating manifest file
        println("Modifying manifest file...")
        createFile(
            assetManager.getManifestFile(),
            androidUtils.manifestFile
        )
        logDone()

        // Creating MainViewModel
        logDoing("Creating MainViewModel.kt ...")
        createFile(
            assetManager.getMainViewModel(),
            androidUtils.mainViewModelFile
        )
        logDone()

        // Delete default main activity
        androidUtils.oldMainActivityFile.delete()

        logDoing("Modifying MainActivity.kt ...")
        // Creating new one
        createFile(
            assetManager.getMainActivity(),
            androidUtils.mainActivityFile
        )
        logDone()


        logDoing("Adding data binding to main layout file...")
        // Creating new layout file data binding
        createFile(
            assetManager.getActivityMainLayout(),
            androidUtils.mainLayoutFile
        )
        logDone()


        logDoing("Updating content_main.xml to support data binding")
        // Creating content main
        createFile(
            assetManager.getContentMainLayout(),
            androidUtils.contentMainLayoutFile
        )
        logDone()

        logDoing("Creating dagger activity builder...")
        // Create activity builder
        createFile(
            assetManager.getActivityBuilder(),
            androidUtils.activityBuilderModuleFile
        )
        logDone()


        logDoing("Creating dagger AppComponent.kt ...")
        // Creating dagger component
        createFile(
            assetManager.getAppComponent(),
            androidUtils.appComponentFile
        )
        logDone()


        if (project.isNeedNetworkModule) {

            logDoing("Creating network module...")
            // Create NetworkModule
            createFile(
                assetManager.getNetworkModule(),
                androidUtils.networkModuleFile
            )
            logDone()

            logDoing("Creating ApiInterface.kt ...")
            // Create ApiInterface
            createFile(
                assetManager.getApiInterface(),
                androidUtils.apiInterfaceFile
            )
            logDone()


            if (project.isNeedLogInScreen) {

                logDoing("Creating LogInRequest.kt ...")
                // Create login request file
                createFile(
                    assetManager.getLogInRequest(),
                    androidUtils.logInRequestFile
                )
                logDone()


                logDoing("Creating LogInResponse.kt ...")
                // Creating login response
                createFile(
                    assetManager.getLogInResponse(),
                    androidUtils.logInResponseFile
                )
                logDone()


                logDoing("Creating UserPrefRepository.kt ...")
                // Create user pref repositories
                createFile(
                    assetManager.getUserRepository(),
                    androidUtils.userRepoFile
                )
                logDone()

                // Create login activity
                logDoing("Creating LogInActivity.kt ...")
                createFile(
                    assetManager.getLogInActivity(),
                    androidUtils.logInActivityFile
                )
                logDone()


                logDoing("Creating LogInViewModel.kt ...")
                // Create login view model
                createFile(
                    assetManager.getLogInViewModel(),
                    androidUtils.logInViewModelFile
                )
                logDone()

                logDoing("Creating LogInClickHandler.kt ...")
                // Create login click handler
                createFile(
                    assetManager.getLogInClickHandler(),
                    androidUtils.loginClickHandler
                )
                logDone()


                logDoing("Creating AuthRepository.kt ...")
                // Create auth repository
                createFile(
                    assetManager.getAuthRepository(),
                    androidUtils.authRepositoryFile
                )
                logDone()


                logDoing("Creating login layout...")
                createFile(
                    assetManager.getLogInLayout(),
                    androidUtils.logInLayoutFile
                )
                logDone()

                logDoing("Creating login related icons")
                AssetManager.userIcon.copyTo(androidUtils.userIconFile)
                AssetManager.logOutIcon.copyTo(androidUtils.logOutIcon)
                logDone()

                // Create string xml
                logDoing("Adding login strings to strings.xml")
                createFile(
                    assetManager.getStringsXml(),
                    androidUtils.stringXmlFile
                )
                logDone()

                // Create menu main
                logDoing("Modifying menu_main.xml file")
                createFile(
                    assetManager.getMenuMain(),
                    androidUtils.menuMainFile
                )
                logDone()
            }
        }


        // Create AppModule
        logDoing("Creating dagger AppModule.kt ...")
        createFile(
            assetManager.getAppModule(),
            androidUtils.appModuleFile
        )
        logDone()


        logDoing("Creating dagger ViewModelModule.kt ...")
        // Create viewModelModule
        createFile(
            assetManager.getViewModelModule(),
            androidUtils.viewModelModuleFile
        )
        logDone()


        if (project.isNeedSplashScreen) {

            // Create splash view model
            logDoing("Creating SplashViewModel.kt")
            createFile(
                assetManager.getSplashViewModel(),
                androidUtils.splashViewModelFile
            )
            logDone()


            logDoing("Creating SplashActivity.kt ...")
            // Create splash activity
            createFile(
                assetManager.getSplashActivity(),
                androidUtils.splashActivityFile
            )
            logDone()

            // Replace with styles
            logDoing("Modifying styles.xml to support splash theme")
            createFile(
                assetManager.getStyles(),
                androidUtils.stylesFile
            )
            logDone()


            // Create splash bg
            logDoing("Creating splash_bg.xml ...")
            createFile(
                assetManager.getSplashBg(),
                androidUtils.splashBgFile
            )
            logDone()

        }


        // Create ids
        logDoing("Creating ids.xml ...")
        createFile(
            assetManager.getIds(),
            androidUtils.idsFile
        )
        logDone()


        logDoing("Creating logo icon...")
        // Create vector icon
        AssetManager.androidIcon.copyTo(androidUtils.androidIcon)
        logDone()

        logDoing("Adding color constants to colors.xml")
        // Create colors
        AssetManager.colorsFile.copyTo(androidUtils.colorsFile, true)
        logDone()


        logDoing("Finishing project setup...")
        logDone()


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
            "data/repositories",

            "di/components",
            "di/modules",

            "models",

            "ui/activities/main",
            "utils"
        )

        if (project.isNeedNetworkModule) {
            dirsToCreate.add("data/remote")
        }

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