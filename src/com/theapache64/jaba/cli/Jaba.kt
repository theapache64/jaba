package com.theapache64.jaba.cli

import com.theapache64.jaba.cli.models.Project
import com.theapache64.jaba.cli.utils.*
import java.io.File
import java.lang.StringBuilder

class Jaba(
    private val androidUtils: AndroidUtils,
    private val project: Project
) {

    companion object {
        const val KOTLIN_PLUGIN = "apply plugin: 'kotlin-android'"
        const val KOTLIN_ANDROID_EXTENSION = "apply plugin: 'kotlin-android-extensions'"

        const val FLOATING_ACTION_BUTTON = "com.google.android.material.floatingactionbutton.FloatingActionButton"
        const val TOOLBAR = "androidx.appcompat.widget.Toolbar"
        const val SYNTHETIC_IMPORT = "import kotlinx.android.synthetic.main.activity_main.*"
    }

    private val mainProPath = project.packageName.replace(".", "/")

    fun build() {

        logDoing("Creating dirs...")
        createDirs()
        logDone("Done")

        // Fix it xml first
        logDoing("Fixing XML style issue...")
        FixItXml.fixIt(project.dir)
        logDone()

        // Remove apply plugin: 'kotlin-android-extensions'
        logDoing("Removing kotlin-android-extension...")
        replaceOn(androidUtils.gradleFile, KOTLIN_ANDROID_EXTENSION, "")
        logDone()

        // Move main activity
        logDoing("Moving MainActivity new dir...")
        val newMainPath = moveMainActivity()
        logDone("Done (moved to $newMainPath)")

        // Change package name of main activity
        logDoing("Changing it's package name and replacing synthetic reference with findViewByid")
        val subPackageName = ".ui.activities.main"
        val mainPackageName = "${project.packageName}$subPackageName"
        changePackageNameAndRemoveSynthetic(newMainPath, mainPackageName)
        logDone()


        // Add R import to main
        logDoing("Fixing missing R issue")
        addImportTo(newMainPath, "${project.packageName}.R")
        logDone()


        // Change package name in manifest of main
        logDoing("Changing package name in several places")
        replaceOn(androidUtils.manifestFile, ".MainActivity", "$subPackageName.MainActivity")

        // Change class name in layout files
        replaceOn(androidUtils.mainLayoutFile, ".MainActivity", "$subPackageName.MainActivity")
        replaceOn(androidUtils.contentMainLayoutFile, ".MainActivity", "$subPackageName.MainActivity")
        logDone()

        logDoing("Adding dependencies and other gradle friendly tasks")
        doGradleThings()
        logDone()

        logDoing("Creating App class...")
        createAndIntegrateAppClass()
        logDone()

        //TODO: createAndIntegrateAppClass
        //TODO: integrateAppClass
        //TODO: createSplashActivity
        //TODO: integrateSplashActivity
        //TODO: createLogInActivity
        //TODO: integrateLogInActivity
        //TODO:

    }

    private fun createAndIntegrateAppClass() {

        // Create
        val appClass = FullFileModels.getAppModel(project.packageName, project.isNeedGoogleFontsModule)
        val appClassFile = File("${androidUtils.provideRootSourcePath()}/App.kt")
        appClassFile.writeText(appClass)

        // integrate
        replaceOn(androidUtils.manifestFile, "<application", "<application\n\t\tandroid:name=\".App\"")
    }

    private fun doGradleThings() {

        // variable-ize

        // Getting current variables
        val vars = androidUtils.getGradleVars()
        val compileSdkVersion = vars.first
        val minSdkVersion = vars.second
        val targetSdkVersion = vars.third
        val kotlinVersion = androidUtils.getKotlinVersion()


        replaceOn(
            androidUtils.gradleFile,
            "compileSdkVersion $compileSdkVersion",
            "compileSdkVersion compile_sdk_version"
        )
        replaceOn(androidUtils.gradleFile, "minSdkVersion $minSdkVersion", "minSdkVersion min_sdk_version")
        replaceOn(androidUtils.gradleFile, "targetSdkVersion $targetSdkVersion", "targetSdkVersion target_sdk_version")

        val versions = """
                ext {
                        kotlin_version = '$kotlinVersion'
                        compile_sdk_version = $compileSdkVersion
                        min_sdk_version = $minSdkVersion
                        target_sdk_version = $targetSdkVersion
                    }

            """.trimIndent()
        replaceOn(androidUtils.projectGradleFile, "ext.kotlin_version = '$kotlinVersion'", versions)

        // Add test comment
        replaceOn(
            androidUtils.gradleFile,
            "implementation fileTree(dir: 'libs', include: ['*.jar'])",
            "\n\t//Core\n\timplementation fileTree(dir: 'libs', include: ['*.jar'])"
        )

        replaceOn(androidUtils.gradleFile, "testImplementation", "\n\t// Test\n\ttestImplementation")

        val dollar = "$"


        // Add kapt
        replaceOn(
            androidUtils.gradleFile,
            "apply plugin: 'kotlin-android'",
            "apply plugin: 'kotlin-android'\napply plugin: 'kotlin-kapt'"
        )

        // Add TwinKill
        var deps = """
                // Lifecycle extension
                    implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0-alpha01'

                    // Dagger 2
                    def daggerVersion = '2.17'
                    implementation "com.google.dagger:dagger:${dollar}daggerVersion"
                    implementation "com.google.dagger:dagger-android-support:${dollar}daggerVersion"
                    kapt "com.google.dagger:dagger-compiler:${dollar}daggerVersion"
                    kapt "com.google.dagger:dagger-android-processor:${dollar}daggerVersion"

                    // MaterialColors
                    implementation 'com.theah64.materialcolors:materialcolors:1.0.0'

                    // TwinKill
                    def twinkill_version = '0.0.1-alpha04'
                    implementation "com.theapache64.twinkill:core:${dollar}twinkill_version"
            """.trimIndent()

        if (project.isNeedNetworkModule) {
            deps += "\n\timplementation \"com.theapache64.twinkill:network:${dollar}twinkill_version\""
        }

        if (project.isNeedGoogleFontsModule) {
            deps += "\n\timplementation \"com.theapache64.twinkill:google_fonts:0.0.1-alpha01\""
        }

        replaceOn(androidUtils.gradleFile, "// Test", deps + "\n\n\t// Test")
    }

    private fun replaceOn(file: File, find: String, replace: String) {
        val fileContent = file.readText()
        val newContent = fileContent.replace(find, replace)
        file.delete()
        file.writeText(newContent)
    }

    private fun addImportTo(filePath: String, packageName: String) {
        val kotlintFile = File(filePath)
        val fileContents = kotlintFile.readText()
        val classNameIndex = fileContents.indexOf("class ${kotlintFile.nameWithoutExtension}")
        require(classNameIndex > 0) { "Couldn't find class name to add import" }
        val finalContent = StringBuilder(fileContents).insert(classNameIndex, "import $packageName\n\n")
        require(kotlintFile.delete()) { "Failed to remove old file" }
        kotlintFile.writeText(finalContent.toString())
    }

    private fun changePackageNameAndRemoveSynthetic(kotlinFilePath: String, newPackageName: String) {
        val kotlinFile = File(kotlinFilePath)
        val fileContents = kotlinFile.readText()
        var newFileContents = fileContents.replace(
            Regex("package .+"), "package $newPackageName"
        )

        newFileContents = newFileContents.replace(
            SYNTHETIC_IMPORT,
            "import $FLOATING_ACTION_BUTTON\nimport $TOOLBAR"
        )
        newFileContents = newFileContents.replace(
            "setSupportActionBar(toolbar)",
            "val toolbar = findViewById<Toolbar>(R.id.toolbar)\n\t\tsetSupportActionBar(toolbar)"
        )
        newFileContents =
            newFileContents.replace("fab", "val fab = findViewById<FloatingActionButton>(R.id.fab)\n\t\tfab")

        // delete old file
        kotlinFile.delete()

        // write new contents
        kotlinFile.writeText(newFileContents)
    }

    /**
     * This will move IDE created MainActivity file to ui/main and returns the new path
     */
    private fun moveMainActivity(): String {


        val mainProPath = project.packageName.replace(".", "/")
        val mainActPath = "${project.dir}/app/src/main/java/$mainProPath/MainActivity.kt"

        val newMainPath = "ui/activities/main/MainActivity.kt"
        val to = "${project.dir}/app/src/main/java/$mainProPath/$newMainPath"
        moveFile(
            mainActPath,
            to
        )



        return to
    }

    /**
     * To move file from one to another destination
     */
    private fun moveFile(from: String, to: String) {
        val oldFile = File(from)
        oldFile.renameTo(File(to))
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
            val fullDir = "${project.dir}/app/src/main/java/$mainProPath/$dir"
            File(fullDir).mkdirs()
        }
    }


}