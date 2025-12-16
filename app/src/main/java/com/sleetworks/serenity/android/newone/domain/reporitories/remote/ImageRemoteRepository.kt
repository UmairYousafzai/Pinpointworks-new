package com.sleetworks.serenity.android.newone.domain.reporitories.remote

import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.network.Resource
import okhttp3.ResponseBody

interface ImageRemoteRepository {

    suspend fun downloadImageThumbFile(imageId: String): Resource<ResponseBody>
    suspend fun getImagesForPoint(pointID: String): Resource<ApiResponse<Map<String, String>>>
    suspend fun getLargeImage(imageID: String): Resource<ResponseBody>
}