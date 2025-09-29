package com.sleetworks.serenity.android.newone.data.storage

import android.content.Context
import android.util.Log
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


abstract class AbstractWorkspaceFileStore {

    companion object {
        const val GLOBAL = "global-data"
    }

    /**
     * Return log tag
     */
    protected abstract fun getTag(): String

    /**
     * Return file system root (cache, internal or external storage)
     */
    protected abstract fun getRootDir(context: Context): File

    /**
     * Check whether a file exists.
     */
    fun fileExists(
        workspace: String,
        subdirectory: String?,
        fileName: String,
        context: Context
    ): Boolean {
        val subdir = getSubdirectory(workspace, subdirectory, false, context)
        return subdir?.let { File(it, fileName).exists() } ?: false
    }

    /**
     * Get a reference to a file, creating the subdirectory if absent, so the camera can save an image to it.
     */
    fun getFileReference(
        workspace: String,
        subdirectory: String?,
        fileName: String,
        context: Context
    ): File {
        val subdir = getSubdirectory(workspace, subdirectory, true, context)
        return File(subdir, fileName)
    }

    /**
     * Create an output stream to a file in the store. This is used to receive an image direct from an HTTP stream without
     * holding it in RAM. The stream must be closed externally.
     */
    fun getOutputStream(
        workspace: String,
        subdirectory: String?,
        fileName: String,
        context: Context
    ): OutputStream {
        val subdir = getSubdirectory(workspace, subdirectory, true, context)
        val file = File(subdir, fileName)
        file.delete()

        return try {
            BufferedOutputStream(FileOutputStream(file))
        } catch (e: FileNotFoundException) {
            throw RuntimeException("Could not find file ${file.absolutePath}")
        }
    }


    fun deleteFile(workspaceId: String, subdirectory: String?, fileName: String, context: Context) {
        val subdir = getSubdirectory(workspaceId, subdirectory, false, context)
        subdir?.let {
            val file = File(it, fileName)
            if (file.exists()) {
                file.delete()
            }
        }
    }

    /**
     * Remove all files for a workspace from the store recursively.
     */
    fun deleteAllFiles(workspaceId: String, context: Context) {
        val dir = getSubdirectory(workspaceId, null, false, context)
        dir?.takeIf { it.exists() }?.let {
            deleteAllFilesInDirectory(it)
        }
    }

    /**
     * Recursive file delete method.
     */
    fun deleteAllFilesInDirectory(subdirectory: File) {
        subdirectory.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                deleteAllFilesInDirectory(file)
            } else {
                file.delete()
            }
        }
    }

    /**
     * Rename a file.
     */
    fun renameFile(
        workspaceId: String,
        subdirectory: String?,
        oldFileName: String,
        newFileName: String,
        context: Context
    ): Boolean {
        val file = getFileReference(workspaceId, subdirectory, oldFileName, context)
        return if (file.exists() && file.isFile) {
            file.renameTo(File(file.parentFile, newFileName))
        } else {
            false
        }
    }

    /**
     * Import a file from somewhere else.
     */
    @Throws(IOException::class)
    fun importFile(
        source: File?,
        workspaceId: String,
        subdirectory: String?,
        fileName: String,
        context: Context
    ) {
        if (source != null && source.exists() && source.isFile) {
            deleteFile(workspaceId, subdirectory, fileName, context)
            val target = getFileReference(workspaceId, subdirectory, fileName, context)

            BufferedInputStream(FileInputStream(source)).use { fIn ->
                BufferedOutputStream(FileOutputStream(target)).use { fOut ->
                    var b: Int
                    while (fIn.read().also { b = it } != -1) {
                        fOut.write(b)
                    }
                    fOut.flush()
                }
            }
        }
    }

    fun copyFile(
        source: File?,
        workspaceId: String,
        subdirectory: String?,
        fileName: String,
        context: Context
    ) {
        if (source != null && source.exists() && source.isFile) {
            deleteFile(workspaceId, subdirectory, fileName, context)
            val target = getFileReference(workspaceId, subdirectory, fileName, context)

            try {
                FileInputStream(source).use { `in` ->
                    FileOutputStream(target).use { out ->
                        val buffer = ByteArray(1024)
                        var read: Int
                        while (`in`.read(buffer).also { read = it } != -1) {
                            out.write(buffer, 0, read)
                        }
                        out.flush()
                    }
                }
            } catch (e: Exception) {
                Log.e(getTag(), e.message.toString())
            }
        }
    }

    /**
     * Used to delete orphaned files after sync. Deletes all files in a subdirectory excluding the given names.
     */
    fun deleteAllWithExclusions(
        workspaceId: String,
        subdirectory: String?,
        excludeFileNames: Set<String>?,
        minimumAgeMillis: Long,
        context: Context
    ) {
        val subdir = getSubdirectory(workspaceId, subdirectory, false, context)
        subdir?.let {
            it.listFiles()?.forEach { file ->
                if ((excludeFileNames == null || !excludeFileNames.contains(file.name)) &&
                    file.lastModified() < System.currentTimeMillis() - minimumAgeMillis
                ) {
                    file.delete()
                }
            }
        }
    }

    fun getSubdirectory(
        workspace: String,
        subdirectory: String?,
        createIfMissing: Boolean,
        context: Context
    ): File? {
        if (workspace.isEmpty()) {
            throw IllegalArgumentException("Workspace ID is required")
        }
        var path = getRootDir(context).absolutePath
        if (!path.endsWith(File.separator)) {
            path += File.separator
        }
        path += workspace
        subdirectory?.let { path += File.separator + it }
        val subdir = File(path)

        return if (subdir.exists() && subdir.isDirectory) {
            subdir
        } else if (createIfMissing) {
            subdir.mkdirs()
            subdir
        } else {
            null
        }
    }
}
