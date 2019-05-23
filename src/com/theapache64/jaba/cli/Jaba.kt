package com.theapache64.jaba.cli

import com.theapache64.jaba.cli.models.Project
import com.theapache64.jaba.cli.utils.FixItXml
import com.theapache64.jaba.cli.utils.FullFileModels
import com.theapache64.jaba.cli.utils.Kradle
import java.io.File
import java.io.IOException
import java.lang.StringBuilder
import javax.xml.parsers.DocumentBuilderFactory

class Jaba(
    private val projectDir: String
) {

    private val gradleFile: File = File("$projectDir/app/build.gradle")

    private var packageName: String? = null
    private var projectName: String? = null


    companion object {
        const val KOTLIN_PLUGIN = "apply plugin: 'kotlin-android'"
    }

    /**
     * Return true if it's an android project.
     */
    fun isAndroidProject(): Boolean {
        return gradleFile.exists()
    }

    fun providePackageName(): String {

        if (packageName == null) {
            try {
                packageName = Kradle.getPackageName(gradleFile)
            } catch (e: IndexOutOfBoundsException) {
                throw IllegalArgumentException("Couldn't find package name from gradle file")
            }
        }

        return packageName as String
    }

    /**
     * Returns true if the project is a kotlin android project.
     */
    fun isKotlinProject(): Boolean {

        val gradleFileContents = gradleFile.readText()
        val mainProPath = providePackageName().replace(".", "/")
        val mainActPath = "$projectDir/app/src/main/java/$mainProPath/MainActivity.kt"

        return gradleFileContents.contains(KOTLIN_PLUGIN) && File(mainActPath).exists()
    }


    /**
     * Return project name from strings.xml
     */
    fun provideProjectName(): String {
        if (projectName == null) {
            val stringsFile = File("$projectDir/app/src/main/res/values/strings.xml")

            // parse XML
            val docBuilderFactory = DocumentBuilderFactory.newInstance()
            val docBuilder = docBuilderFactory.newDocumentBuilder()
            val doc = docBuilder.parse(stringsFile)
            val root = doc.documentElement
            val strings = root.getElementsByTagName("string")
            for (i in 0 until strings.length) {
                val stringNode = strings.item(i) as org.w3c.dom.Element
                if (stringNode.getAttribute("name") == "app_name") {
                    this.projectName = stringNode.firstChild.nodeValue
                    break
                }
            }

            if (projectName == null) {
                throw IOException("Couldn't get project name from strings")
            }
        }

        return projectName as String
    }


    /**
     * This method will start building the project from given params.
     */
    fun startBuilding(project: Project) {

        // First create dirs
        ProjectBuilder(project).build()
    }


    class ProjectBuilder(
        private val project: Project
    ) {

        companion object {
            const val FLOATING_ACTION_BUTTON = "com.google.android.material.floatingactionbutton.FloatingActionButton"
            const val TOOLBAR = "androidx.appcompat.widget.Toolbar"
            const val SYNTHETIC_IMPORT = "import kotlinx.android.synthetic.main.activity_main.*"
            const val KOTLIN_ANDROID_EXTENSION = "apply plugin: 'kotlin-android-extensions'"
        }

        private val mainProPath = project.packageName.replace(".", "/")

        fun build() {
            createDirs()

            // Fix it xml first
            FixItXml.fixIt(project.dir)

            // Remove apply plugin: 'kotlin-android-extensions'
            replaceOn(project.gradleFile, KOTLIN_ANDROID_EXTENSION, "")

            // Move main activity
            val newMainPath = moveMainActivity()

            // Change package name of main activity
            val subPackageName = ".ui.activities.main"
            val mainPackageName = "${project.packageName}$subPackageName"
            changePackageNameAndRemoveSynthetic(newMainPath, mainPackageName)


            // Add R import to main
            addImportTo(newMainPath, "${project.packageName}.R")

            // Change package name in manifest of main
            replaceOn(project.manifestFile, ".MainActivity", "$subPackageName.MainActivity")

            // Change class name in layout files
            replaceOn(project.mainLayoutFile, ".MainActivity", "$subPackageName.MainActivity")
            replaceOn(project.contentMainLayoutFile, ".MainActivity", "$subPackageName.MainActivity")

            doGradleThings()
            createAppClass()

            //TODO: createAppClass
            //TODO: integrateAppClass
            //TODO: createSplashActivity
            //TODO: integrateSplashActivity
            //TODO: createLogInActivity
            //TODO: integrateLogInActivity
            //TODO:

        }

        private fun createAppClass() {

            // Create
            val appClass = FullFileModels.getAppModel(project.packageName, project.isNeedGoogleFontsModule)
            val appClassFile = File("${project.rootSrcPath}/App.kt")
            appClassFile.writeText(appClass)

            // integrate
            replaceOn(project.manifestFile, "<application", "<application\n\t\tandroid:name=\".App\"")
        }

        private fun doGradleThings() {

            // variable-ize

            // Getting current variables
            val vars = Kradle.getGradleVars(project.gradleFile)

            val compileSdkVersion = vars.first
            val minSdkVersion = vars.second
            val targetSdkVersion = vars.third
            val kotlinVersion = Kradle.getKotlinVersion(project.projectGradleFile)


            replaceOn(
                project.gradleFile,
                "compileSdkVersion $compileSdkVersion",
                "compileSdkVersion compile_sdk_version"
            )
            replaceOn(project.gradleFile, "minSdkVersion $minSdkVersion", "minSdkVersion min_sdk_version")
            replaceOn(project.gradleFile, "targetSdkVersion $targetSdkVersion", "targetSdkVersion target_sdk_version")

            val versions = """
                ext {
                        kotlin_version = '$kotlinVersion'
                        compile_sdk_version = $compileSdkVersion
                        min_sdk_version = $minSdkVersion
                        target_sdk_version = $targetSdkVersion
                    }

            """.trimIndent()
            replaceOn(project.projectGradleFile, "ext.kotlin_version = '$kotlinVersion'", versions)

            // Add test comment
            replaceOn(
                project.gradleFile,
                "implementation fileTree(dir: 'libs', include: ['*.jar'])",
                "\n\t//Core\n\timplementation fileTree(dir: 'libs', include: ['*.jar'])"
            )

            replaceOn(project.gradleFile, "testImplementation", "\n\t// Test\n\ttestImplementation")

            val dollar = "$"


            // Add kapt
            replaceOn(
                project.gradleFile,
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

            replaceOn(project.gradleFile, "// Test", deps + "\n\n\t// Test")
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

            println("Moving main activity...")

            val mainProPath = project.packageName.replace(".", "/")
            val mainActPath = "${project.dir}/app/src/main/java/$mainProPath/MainActivity.kt"

            val newMainPath = "ui/activities/main/MainActivity.kt"
            val to = "${project.dir}/app/src/main/java/$mainProPath/$newMainPath"
            moveFile(
                mainActPath,
                to
            )

            println("Main activity moved to $newMainPath")

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

            println("Creating dirs...")

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
                println("\t$dir")
                File(fullDir).mkdirs()
            }
        }

    }
}