package com.theapache64.jaba.cli.utils

import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipInputStream


object FileUtils {

    fun unzip(file: File, destination: String) {

        File(destination).mkdirs()

        file.inputStream().use { fis ->

            ZipInputStream(fis).use { zis ->

                // Opening first entry
                var zipEntry = zis.nextEntry

                val buffer = ByteArray(1024)

                while (zipEntry != null) {

                    val newFile = File("$destination${File.separator}${zipEntry.name}")

                    if (zipEntry.isDirectory) {
                        // simply create a directory
                        newFile.mkdirs()
                    } else {

                        // it's a file
                        val fos = FileOutputStream(newFile)
                        var len = zis.read(buffer)
                        while (len > 0) {
                            fos.write(buffer, 0, len)
                            len = zis.read(buffer)
                        }
                        fos.close()
                    }

                    // Closing current entry
                    zis.closeEntry()

                    // Opening next entry
                    zipEntry = zis.nextEntry

                }

                // Close last entry
                zis.closeEntry()
            }
        }


    }

    /*fun unzip(zipFilePath: String, destDir: String) {
        val dir = File(destDir)
        // create output directory if it doesn't exist
        if (!dir.exists()) dir.mkdirs()
        val fis: FileInputStream
        //buffer for read and write data to file
        val buffer = ByteArray(1024)
        try {
            fis = FileInputStream(zipFilePath)
            val zis = ZipInputStream(fis)
            var ze: ZipEntry? = zis.nextEntry
            while (ze != null) {
                val fileName = ze.name
                val newFile = File(destDir + File.separator + fileName)
                println("Unzipping to " + newFile.absolutePath)
                //create directories for sub directories in zip
                File(newFile.parent).mkdirs()
                val fos = FileOutputStream(newFile)
                var len = zis.read(buffer)
                while (len > 0) {
                    fos.write(buffer, 0, len)
                    len = zis.read(buffer)
                }
                fos.close()
                //close this ZipEntry
                zis.closeEntry()
                ze = zis.nextEntry
            }
            //close last ZipEntry
            zis.closeEntry()
            zis.close()
            fis.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }*/


    /**
     * To delete a directory
     */
    fun deleteDir(path: String) {
        val x = File(path)
        if (x.isDirectory) {

            // delete file inside it
            x.listFiles().forEach { y ->
                if (y.isDirectory) {

                    // nested dir
                    deleteDir(y.absolutePath)
                }
                y.delete()
            }
        }

        // At the end, deleting the file/directory
        x.delete()
    }

    fun copyOneLevelDir(source: File, destination: String) {
        source.listFiles().forEach { file ->
            file.copyTo(File("$destination/${file.name}"))
        }
    }

    fun find(fileName: String, inDir: String, matches: MutableList<File> = mutableListOf()): List<File> {
        val dir = File(inDir)
        dir.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                find(fileName, file.absolutePath, matches)
            } else {
                if (file.name == fileName) {
                    matches.add(file)
                }
            }
        }
        return matches
    }

}