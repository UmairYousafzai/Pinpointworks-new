package com.sleetworks.serenity.android.newone.data.storage

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileFilter
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.Serializable
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicLong

/**
 * Storage for files that don't belong to a workspace. Includes workspace and site lists, plus users.
 */
abstract class AbstractRootFileStore {

    /** Return log tag */
    protected abstract fun getTag(): String

    /** Return file system root (cache, internal or external storage) */
    protected abstract fun getRootDir(context: Context): File?

    /**
     * Check whether a file exists.
     */
    fun fileExists(subdirectory: String, fileName: String, context: Context): Boolean {
        val subdir = getSubdirectory(subdirectory, false, context)
        return subdir?.let { File(it, fileName).exists() } ?: false
    }

    /**
     * Count total size of files.
     */
    fun getTotalSize(context: Context): Long {
        val runningTotal = AtomicLong(0)
        recursiveCountDirectorySize(getRootDir(context), runningTotal)
        return runningTotal.get()
    }

    /**
     * Get a reference to a file, creating the subdirectory if absent, so the camera can save an image to it.
     */
    fun getFileReference(subdirectory: String, fileName: String, context: Context): File {
        val subdir = getSubdirectory(subdirectory, true, context)
        return File(subdir, fileName)
    }

    /**
     * Create an output stream to a file in the store. This is used to receive an image direct from an HTTP stream without
     * holding it in RAM. The stream must be closed externally.
     */
    fun getOutputStream(subdirectory: String, fileName: String, context: Context): OutputStream {
        val subdir = getSubdirectory(subdirectory, true, context)
        val file = File(subdir, fileName).apply { delete() }

        return try {
            BufferedOutputStream(FileOutputStream(file))
        } catch (e: FileNotFoundException) {
            throw RuntimeException("Could not find file ${file.absolutePath}")
        }
    }

    /**
     * Store an object by serializing it as JSON in approximately the same form it was received from the server (barring
     * field differences).
     */
    fun storeAsJson(`object`: Any, subdirectory: String, fileName: String, context: Context) {
        val gson = Gson()
        val json = gson.toJson(`object`)
        val subdir = getSubdirectory(subdirectory, true, context)
        val file = File(subdir, fileName).apply { delete() }

        try {
            FileOutputStream(file).use {
                it.write(json.toByteArray(Charsets.UTF_8))
            }
        } catch (e: Exception) {
            Log.e(getTag(), "Could not save object to ${file.absolutePath}", e)
        }
    }

    fun <T> readFromJson(subdirectory: String, fileName: String, type: Type, context: Context): T? {
        val subdir = getSubdirectory(subdirectory, true, context) ?: return null
        val file = File(subdir, fileName)
        if (!file.exists()) return null

        return try {
            FileInputStream(file).use { input ->
                val gson = Gson()
                gson.fromJson(JsonReader(InputStreamReader(input)), type)
            }
        } catch (e: Exception) {
            Log.e(getTag(), "Could not read object from ${file.absolutePath}", e)
            null
        }
    }

    fun deleteFile(subdirectory: String, fileName: String, context: Context) {
        val subdir = getSubdirectory(subdirectory, false, context) ?: return
        val file = File(subdir, fileName)
        if (file.exists()) file.delete()
    }

    /**
     * Remove all files from the store recursively.
     */
    fun deleteAllFiles(context: Context) {
        val dir = getSubdirectory(null, false, context)
        dir?.let { deleteAllFilesInDirectory(it) }
    }

    /**
     * Recursive file delete method.
     */
    private fun deleteAllFilesInDirectory(subdirectory: File) {
        subdirectory.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                deleteAllFilesInDirectory(file)
            } else {
                file.delete()
            }
        }
    }

    /**
     * Rename a file. Typically used when a new image has been uploaded - we can keep the local files instead of downloading
     * the new remote ones.
     */
    fun renameFile(subdirectory: String, oldFileName: String, newFileName: String, context: Context): Boolean {
        val file = getFileReference(subdirectory, oldFileName, context)
        return file.exists() && file.isFile && file.renameTo(File(file.parentFile, newFileName))
    }

    /**
     * Import a file from somewhere else. Used to copy gallery images into app storage so they can't be so easily deleted
     * and can be kept in the cache after upload.
     */
    @Throws(IOException::class)
    fun importFile(source: File, subdirectory: String, fileName: String, context: Context) {
        if (source.exists() && source.isFile) {
            deleteFile(subdirectory, fileName, context)
            val target = getFileReference(subdirectory, fileName, context)

            BufferedInputStream(FileInputStream(source)).use { fIn ->
                BufferedOutputStream(FileOutputStream(target)).use { fOut ->
                    var byte: Int
                    while (fIn.read().also { byte = it } != -1) {
                        fOut.write(byte)
                    }
                    fOut.flush()
                }
            }
        }
    }

    /**
     * Used to delete orphaned files after sync. Deletes all files in a subdirectory excluding the given names.
     */
    fun deleteAllWithExclusions(subdirectory: String, excludeFileNames: Set<String>?, context: Context) {
        val subdir = getSubdirectory(subdirectory, false, context) ?: return
        subdir.listFiles()?.forEach { file ->
            if (excludeFileNames == null || !excludeFileNames.contains(file.name)) {
                file.delete()
            }
        }
    }

    private fun getSubdirectory(subdirectory: String?, createIfMissing: Boolean, context: Context): File? {
        val path = StringBuilder(getRootDir(context)?.absolutePath?:"").apply {
            if (!subdirectory.isNullOrEmpty()) {
                if (!endsWith(File.separator)) append(File.separator)
                append(subdirectory)
            }
        }.toString()

        val subdir = File(path)
        return when {
            subdir.exists() && subdir.isDirectory -> subdir
            createIfMissing -> {
                subdir.mkdirs()
                subdir
            }
            else -> null
        }
    }

    private fun recursiveCountDirectorySize(directory: File?, runningTotal: AtomicLong) {
        if (directory?.exists() == true) {
            directory.listFiles()?.forEach { sub ->
                if (sub.isDirectory) {
                    recursiveCountDirectorySize(sub, runningTotal)
                } else {
                    runningTotal.addAndGet(sub.length())
                }
            }
        }
    }

    fun getFileList(context: Context): List<FileInfo> {
        val list = mutableListOf<FileInfo>()
        val root = getSubdirectory(null, false, context)
        root?.let { appendFileList("", it, list) }
        return list
    }

    private fun appendFileList(path: String, directory: File, list: MutableList<FileInfo>) {
        directory.listFiles(IsFileFilter())?.forEach { file ->
            list.add(FileInfo(file.absolutePath, path, file.name, file.length()))
        }
        directory.listFiles(IsDirectoryFilter())?.forEach { subdirectory ->
            appendFileList("$path${subdirectory.name}/", subdirectory, list)
        }
    }

    private class IsDirectoryFilter : FileFilter {
        override fun accept(file: File) = file.exists() && file.isDirectory
    }

    private class IsFileFilter : FileFilter {
        override fun accept(file: File) = file.exists() && !file.isDirectory
    }

    data class FileInfo(
        val absolutePath: String,
        val subdir: String,
        val name: String,
        val size: Long
    ) : Serializable
}
