package com.sleetworks.serenity.android.newone.data.imageStore


import android.content.Context
import android.util.Base64
import com.sleetworks.serenity.android.newone.data.storage.ExternalStorageUtils
import com.sleetworks.serenity.android.newone.data.storage.InternalWorkspaceStorageUtils
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class UserImageStore @Inject constructor(private val context: Context) {

    /**
     * Get output stream for a user avatar file, ready for download.
     */
    fun getAvatarOutputStream(imageId: String): OutputStream {
        return ExternalStorageUtils().getOutputStream("avatar", imageId, context)
    }

    /**
     * Get a reference to the file for a user avatar, if it exists. Otherwise return null.
     */
    fun getAvatarFile(imageId: String): File? {
        return if (ExternalStorageUtils().fileExists("avatar", imageId, context)) {
            ExternalStorageUtils().getFileReference("avatar", imageId, context)
        } else {
            null
        }
    }

    /**
     * Check a reference to the file for a user avatar and download it if it doesn't exist.
     */
//    fun checkAvatarFile(avatarImageId: String?, setNewUserAvatar: Boolean) {
//        if (!avatarImageId.isNullOrEmpty()) {
//            if (setNewUserAvatar) {
//                SharedPrefsHelper.setUserImageId(context, avatarImageId)
//            }
//            val imageFile = getAvatarFile(avatarImageId, context)
//            if (imageFile == null && PinpointWorksClient.isNetworkAvailable(context)) {
//                // Using a modern alternative like WorkManager or Coroutine is recommended instead of starting a service
//                val msgIntent = Intent(context, AvatarCachingService::class.java).apply {
//                    putExtra(AvatarCachingService.IMAGE_REMOTE_ID, avatarImageId)
//                }
//                context.startService(msgIntent)  // If using background work with Service (deprecated)
//            }
//        }
//    }

    /**
     * Cleanup method used when avatar image becomes redundant.
     */
    fun deleteAvatar(imageId: String) {
        ExternalStorageUtils().deleteFile("avatar", imageId, context)
    }

    /**
     * Remove all avatars except those with the given IDs.
     */
    fun deleteOrphanedAvatars(usedImageIds: Set<String>) {
        ExternalStorageUtils().deleteAllWithExclusions("avatar", usedImageIds, context)
    }

    fun checkImage(
        workspaceId: String,
        subdirectory: String,
        imageId: String,
    ): File {
        return InternalWorkspaceStorageUtils().getFileReference(
            workspaceId, subdirectory,
            "$imageId.png",
            context
        )
    }

    fun saveImage(imageId: String, imageData: String, workspaceId: String) {
        /**
         * decode from string to byte array
         */

        val imgBytesData = Base64.decode(
            imageData,
            Base64.DEFAULT
        )

        val file = InternalWorkspaceStorageUtils().getFileReference(
            workspaceId,
            "$workspaceId/images/thumb/",
            "$imageId.png",
            context
        )

        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        val bufferedOutputStream = BufferedOutputStream(
            fileOutputStream
        )
        try {
            bufferedOutputStream.write(imgBytesData)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bufferedOutputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun saveLargeImage(
        imageID: String, byteStream: InputStream, workspaceId: String,
        subdirectory: String,
    ) {
        val file = checkImage(workspaceId, subdirectory, imageID)

        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        try {
            val outputStream: OutputStream =
                BufferedOutputStream(FileOutputStream(file.absolutePath))

            var inputStream: BufferedInputStream? = null
            inputStream = BufferedInputStream(byteStream)

            var b: Int
            while ((inputStream.read().also { b = it }) != -1) {
                outputStream.write(b)
            }
            outputStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

     fun deleteThumbnail(workspaceId: String, imageId: String) {
        ExternalStorageUtils().deleteFile(workspaceId, "images/thumb", imageId, context)
    }

    fun deleteOriginal(workspaceId: String, imageId: String) {
        ExternalStorageUtils().deleteFile(workspaceId, "images/original", imageId, context)
    }

}
