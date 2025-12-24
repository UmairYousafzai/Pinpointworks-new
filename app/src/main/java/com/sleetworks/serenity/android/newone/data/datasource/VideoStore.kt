package com.sleetworks.serenity.android.newone.data.datasource

import android.content.Context
import com.sleetworks.serenity.android.newone.data.storage.ExternalStorageUtils
import com.sleetworks.serenity.android.newone.data.storage.InternalWorkspaceStorageUtils
import java.io.File
import java.io.OutputStream
import javax.inject.Inject

class VideoStore @Inject constructor(private val context: Context){
    fun getOriginalFileReference(workspaceId: String, videoId: String): File {
        return ExternalStorageUtils().getFileReference(
            workspaceId,
            "$workspaceId/video/original",
            "$videoId.mp4",
            context
        )
    }
    fun checkVideo(
        workspaceId: String,
        subdirectory: String,
        videoId: String,
    ): File {
        return InternalWorkspaceStorageUtils().getFileReference(
            workspaceId, subdirectory,
            "$videoId.mp4",
            context
        )
    }
    fun getOriginalOutputStream(
        workspaceId: String,
        videoID: String,
    ): OutputStream {
        return ExternalStorageUtils().getOutputStream(
            workspaceId,
            "$workspaceId/video/original",
            "$videoID.mp4",
            context
        )
    }

    /**
     * Cleanup method used when original video becomes redundant.
     */
    fun deleteOriginal(workspaceId: String, videoId: String,) {
        ExternalStorageUtils().deleteFile(workspaceId, "video_original", videoId, context)
    }

    /**
     * Cleanup method used when original video becomes redundant.
     */
    fun deleteThumbnail(workspaceId: String, videoId: String,) {
        ExternalStorageUtils().deleteFile(workspaceId, "video_thumbnail", videoId, context)
    }



}