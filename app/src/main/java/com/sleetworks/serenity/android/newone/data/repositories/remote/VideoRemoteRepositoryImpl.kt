package com.sleetworks.serenity.android.newone.data.repositories.remote

import android.content.Context
import com.sleetworks.serenity.android.newone.data.datasource.remote.ApiService
import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Video
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.data.network.RetrofitProvider
import com.sleetworks.serenity.android.newone.data.network.safeApiCall
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.VideoRemoteRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

class VideoRemoteRepositoryImpl @Inject constructor(
    private val context: Context,
    private val retrofitProvider: RetrofitProvider
) :
    VideoRemoteRepository {
    override suspend fun uploadVideo(
        workspaceId: String,
        pointId: String,
        pointCaption: String,
        videoFile: File
    ): Resource<ApiResponse<Video>> {
        val itemRefId = pointId.toRequestBody("text/plain".toMediaTypeOrNull())
        val itemRefType = "DefectType".toRequestBody("text/plain".toMediaTypeOrNull())
        val itemRefCaption = pointCaption.toRequestBody("text/plain".toMediaTypeOrNull())

        val mediaType = "video/mp4".toMediaTypeOrNull()
        val requestFile = videoFile.asRequestBody(mediaType)

        val videoPart = MultipartBody.Part.createFormData(
            "video",
            videoFile.name,
            requestFile
        )
        return safeApiCall(context) {
            retrofitProvider.getRetrofit().create(ApiService::class.java)
                .uploadVideo(
                    itemRefId = itemRefId,
                    itemRefType = itemRefType,
                    itemRefCaption = itemRefCaption,
                    workspaceId = workspaceId,
                    imageFile = videoPart
                )
        }
    }

    override suspend fun downloadVideo(videoId: String): Resource<ResponseBody> {

        return safeApiCall(context) {
            retrofitProvider.getRetrofit().create(ApiService::class.java).downloadVideo(videoId)
        }
    }

    override suspend fun deleteVideo(
        pointId: String,
        videoId: String
    ): Resource<ApiResponse<String>> {
        return safeApiCall(context) {
            retrofitProvider.getRetrofit().create(ApiService::class.java)
                .removeVideo(videoId, pointId)
        }
    }

}