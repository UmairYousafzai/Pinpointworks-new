package com.sleetworks.serenity.android.newone.data.imageStore


import android.content.Context
import com.sleetworks.serenity.android.newone.data.storage.ExternalStorageUtils
import java.io.File
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
}
