package com.theapache64.jaba.cli

import com.theapache64.jaba.cli.models.Project
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
                packageName = gradleFile.readText().split("applicationId \"")[1].split("\"")[0]
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
                throw IOException("dfg")
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

        private val mainProPath = project.packageName.replace(".", "/")

        fun build() {
            createDirs()

            // MainActivity
            val newMainPath = moveMainActivity()
            val subPackageName = ".ui.activities.main"
            val mainPackageName = "${project.packageName}$subPackageName"
            changePackageName(newMainPath, mainPackageName)
            addImportTo(newMainPath, "${project.packageName}.R")
            val manifestFile = File("${project.dir}/app/src/main/AndroidManifest.xml")
            replaceOn(manifestFile, ".MainActivity", "$subPackageName.MainActivity")
            //TODO: addTwinKill deps
            //TODO: createAppClass
            //TODO: integrateAppClass
            //TODO: createSplashActivity
            //TODO: integrateSplashActivity
            //TODO: createLogInActivity
            //TODO: integrateLogInActivity
            //TODO:

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

        private fun changePackageName(kotlinFilePath: String, newPackageName: String) {
            val kotlinFile = File(kotlinFilePath)
            val fileContents = kotlinFile.readText()
            val newFileContents = fileContents.replace(
                Regex("package .+"), "package $newPackageName"
            )

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