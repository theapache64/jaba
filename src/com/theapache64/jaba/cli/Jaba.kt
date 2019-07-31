package com.theapache64.jaba.cli

import com.google.gson.Gson
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
        private val PACKAGE_NAME_REGEX by lazy { Pattern.compile("package (?<packageName>.+)") }
        private val MIN_SDK_REGEX by lazy { Pattern.compile("minSdkVersion (\\d+)") }
        private val TARGET_SDK_REGEX by lazy { Pattern.compile("targetSdkVersion (\\d+)") }
        private val ANDROIDX_APPCOMPAT_REGEX by lazy { Pattern.compile("implementation 'androidx\\.appcompat:appcompat:(.+)'") }
        private val KTX_VERSION_REGEX by lazy { Pattern.compile("implementation 'androidx\\.core:core-ktx:(.+)'") }
        private val CONSTRAINT_VERSION_REGEX by lazy { Pattern.compile("implementation 'androidx\\.constraintlayout:constraintlayout:(.+)'") }
        private val MATERIAL_VERSION_REGEX by lazy { Pattern.compile("implementation 'com\\.google\\.android\\.material:material:(.+)'") }
        private val JUNIT_VERSION_REGEX by lazy { Pattern.compile("testImplementation 'junit:junit:(.+)'") }
        private val ESPRESSO_VERSION_REGEX by lazy { Pattern.compile("androidTestImplementation 'androidx\\.test\\.espresso:espresso-core:(.+)'") }

        private val KOTLIN_VERSION_REGEX by lazy { Pattern.compile("ext.kotlin_version = '(.+)'") }
        private val GRADLE_VERSION_REGEX by lazy { Pattern.compile("classpath 'com.android.tools.build:gradle:(.+)'") }

        fun toSnackCase(input: String): String {
            return input.replace(Regex("([a-z])([A-Z]+)"), "$1_$2").toLowerCase()
        }

        fun provideActivitySupport(currentDir: String, activityFile: File, componentName: String) {
            val projectFile = File("$currentDir/jaba_project.json")
            if (projectFile.exists()) {

                println("Decoding project JSON...")
                val projectJson = projectFile.readText()
                val project = Gson().fromJson(projectJson, Project::class.java)

                println("Project : ${project.name}")
                println("Package : ${project.packageName}")
                println()

                val assetManager = AssetManager(project)

                val fullPackageName = getPackageNameFromKotlin(activityFile)

                if (fullPackageName != null) {

                    // Creating new activity
                    logDoing("Upgrading activity...")
                    createFile(
                        assetManager.getActivity(project.packageName, fullPackageName, componentName),
                        activityFile
                    )
                    logDone()

                    // Creating ViewModel
                    val newViewModelFile = File("${activityFile.parent}/${componentName}ViewModel.kt")

                    logDoing("Creating ViewModel...")
                    createFile(
                        assetManager.getViewModel(fullPackageName, componentName),
                        newViewModelFile
                    )
                    logDone()



                    logDoing("Creating Handler...")
                    val handlerFile = File("${activityFile.parent}/${componentName}Handler.kt")
                    createFile(
                        assetManager.getHandler(fullPackageName, componentName),
                        handlerFile
                    )
                    logDone()

                    logDoing("Upgrading layout file...")
                    val androidUtils = AndroidUtils(currentDir)
                    val componentNameSnakeCase = StringUtils.camelCaseToSnackCase(componentName)
                    val layoutFile = androidUtils.getLayoutFile("activity_${componentNameSnakeCase}.xml")
                    createFile(
                        assetManager.getLayoutFile(fullPackageName, componentName),
                        layoutFile
                    )
                    logDone()


                    logDoing("Adding new activity builder for ${componentName}Activity")

                    // Add SomeActivity builder in ActivityBuilderModule
                    val actBuilderFileContent = androidUtils.activityBuilderModuleFile.readText()
                    val lastIndexOfCbra = actBuilderFileContent.lastIndexOf("}")
                    if (lastIndexOfCbra != -1) {
                        val builderMethod = assetManager.getActivityBuilder(componentName)
                        val builder = StringBuilder(actBuilderFileContent)
                        val newFileContent = builder.insert(
                            lastIndexOfCbra - 1,
                            builderMethod
                        )

                        createFile(
                            newFileContent.toString(),
                            androidUtils.activityBuilderModuleFile
                        )
                        logDone()


                        logDoing("Adding new viewModel to builder for ${componentName}ViewModel")

                        // Add SomeActivity builder in ActivityBuilderModule
                        val vmBuilderFileContent = androidUtils.viewModelModuleFile.readText()
                        val lastIndexOfBuilder = vmBuilderFileContent.lastIndexOf("}")
                        if (lastIndexOfBuilder != -1) {
                            val vmBuilderMethod = assetManager.getVmBuilder(componentName)
                            val vmBuilder = StringBuilder(vmBuilderFileContent)
                            val newVmBuilderContent = vmBuilder.insert(
                                lastIndexOfBuilder - 1,
                                vmBuilderMethod
                            )


                            createFile(
                                newVmBuilderContent.toString(),
                                androidUtils.viewModelModuleFile
                            )
                            logDone()

                            logDone("Finish")

                        } else {
                            error("Couldn't find ViewModelBuilder.kt's end. No curly braces (}) found in the file.")
                        }


                    } else {
                        error("Couldn't find ActivitiesBuilderModule.kt's end. No curly braces (}) found in the file.")
                    }




                } else {
                    error("Couldn't get full package name from activity file")
                }

            } else {
                error("$currentDir is not a jaba project. Init jaba by running `jaba` in the project root")
            }
        }

        private fun getPackageNameFromKotlin(kotlinFile: File): String? {
            val fileContent = kotlinFile.readText()
            val matcher = PACKAGE_NAME_REGEX.matcher(fileContent)
            if (matcher.find()) {
                return matcher.group("packageName")
            }
            return null
        }


        private fun createFile(fileContent: String, file: File) {

            // Deleting old file
            file.delete()

            // Deleting parent dirs
            file.parentFile.mkdirs()

            // Writing to file
            file.writeText(fileContent)
        }


    }


    fun build() {

        // Create jaba_project.json
        createProjectJson()

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

            // Create splash activity xml
            println("Creating splash activity layout")
            createFile(
                assetManager.getSplashLayout(),
                androidUtils.splashActivityLayoutFile
            )
            logDone()

            // Replace with styles
            logDoing("Modifying styles.xml to support splash theme")
            createFile(
                assetManager.getStyles(),
                androidUtils.stylesFile
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

        // Finally checking if the user wanted to change the MainActivity name to something else
        project.newMainName?.let { newMainName ->
            logDoing("Changing Main to $newMainName")
            changeMainTo(newMainName)
            logDone()
        }

        logDoing("Finishing project setup...")
        logDone()


    }

    private fun createProjectJson() {

        val projectJson = Gson().toJson(project)
        val jabaProjectJsonFile = File("$currentDir/jaba_project.json")
        if (jabaProjectJsonFile.exists()) {
            jabaProjectJsonFile.delete()
        }

        jabaProjectJsonFile.writeText(projectJson)
    }

    private fun changeMainTo(newMainName: String) {

        val newMainNameWithOutAct = newMainName.replace("Activity", "")
        val compSnackCase = toSnackCase(newMainNameWithOutAct)

        // Change MainActivity name
        val newActFile = File("${androidUtils.mainActivityFile.parent}/$newMainName.kt")
        require(androidUtils.mainActivityFile.renameTo(newActFile)) { "Failed to rename MainActivty.kt to ${newActFile.name}" }

        // Change ViewModel name
        val newViewModelName = "${newMainNameWithOutAct}ViewModel";
        val newViewModel = File("${androidUtils.mainViewModelFile.parent}/$newViewModelName.kt")
        require(androidUtils.mainViewModelFile.renameTo(newViewModel)) { "Failed to rename MainViewModel" }

        // Change layout file name
        val newLayoutName = "activity_$compSnackCase"
        val newLayoutFile = File("${androidUtils.mainLayoutFile.parent}/$newLayoutName.xml")
        require(androidUtils.mainLayoutFile.renameTo(newLayoutFile)) { "Failed to rename main layout file" }

        // Change content layout name
        val newContentLayoutName = "content_$compSnackCase"
        val newContentLayoutFile = File("${androidUtils.contentMainLayoutFile.parent}/$newContentLayoutName.xml")
        require(androidUtils.contentMainLayoutFile.renameTo(newContentLayoutFile)) { "Failed to rename content layout file" }

        // Change main menu name
        val newMenuName = "menu_$compSnackCase"
        val newMenuFile = File("${androidUtils.menuMainFile.parent}/$newMenuName.xml")
        require(androidUtils.menuMainFile.renameTo(newMenuFile)) { "Failed to rename main_menu file" }


        // Change activity name in manifest
        changeContent(
            androidUtils.manifestFile,
            "MainActivity",
            newMainName
        )

        // Change names in activity
        changeContent(
            newActFile,
            mapOf(
                Pair("activity_main", "activity_$compSnackCase"),
                Pair("MainActivity", newMainName),
                Pair("menu_main", newMenuName),
                Pair("MainViewModel", newViewModelName),
                Pair("ActivityMainBinding", "Activity${newMainNameWithOutAct}Binding")
            )
        )


        // Change names in main layout file
        changeContent(
            newLayoutFile,
            mapOf(
                Pair("MainViewModel", newViewModelName),
                Pair("MainActivity", newMainName),
                Pair("i_content_main", "i_$newContentLayoutName"),
                Pair("content_main", newContentLayoutName)
            )
        )

        // Change names in content layout
        changeContent(
            newContentLayoutFile,
            mapOf(
                Pair("MainViewModel", newViewModelName),
                Pair("MainActivity", newMainName),
                Pair("activity_main", newLayoutName)
            )
        )

        // Change in viewModel
        changeContent(
            newViewModel,
            mapOf(
                Pair("MainViewModel", newViewModelName)
            )
        )

        changeContent(
            androidUtils.activityBuilderModuleFile,
            "MainActivity",
            newMainName
        )

        changeContent(
            androidUtils.viewModelModuleFile,
            "MainViewModel",
            newViewModelName
        )

        if (project.isNeedLogInScreen) {

            changeContent(
                androidUtils.logInActivityFile,
                "MainActivity", newMainName
            )

        }

        if (project.isNeedSplashScreen) {

            changeContent(
                androidUtils.splashActivityFile,
                "MainActivity", newMainName
            )

            changeContent(
                androidUtils.splashViewModelFile,
                "MainActivity", newMainName
            )
        }

    }

    private fun changeContent(file: File, map: Map<String, String>) {
        var newContent = file.readText()
        for (item in map) {
            newContent = newContent.replace(item.key, item.value)
        }
        file.writeText(newContent)
    }

    private fun changeContent(file: File, find: String, replace: String) {
        changeContent(
            file, mapOf(
                Pair(find, replace)
            )
        )
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
            espressoVersion!!,
            gradleVersion!!
        )

        androidUtils.projectGradleFile.writeText(newProjectGradle)
    }

}