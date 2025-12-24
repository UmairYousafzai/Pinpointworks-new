package com.sleetworks.serenity.android.newone.domain.reporitories.remote

import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Video
import com.sleetworks.serenity.android.newone.data.network.Resource
import okhttp3.ResponseBody
import java.io.File

interface VideoRemoteRepository {

    suspend fun uploadVideo(
        workspaceId: String,
        pointId: String,
        pointCaption: String,
        videoFile: File
    ): Resource<ApiResponse<Video>>

    suspend fun downloadVideo(videoId: String): Resource<ResponseBody>
    suspend fun deleteVideo(pointId: String, videoId: String): Resource<ApiResponse<String>>

}