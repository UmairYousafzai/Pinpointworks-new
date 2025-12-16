package com.sleetworks.serenity.android.newone.data.repositories.remote

import android.content.Context
import com.sleetworks.serenity.android.newone.data.datasource.remote.ApiService
import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.data.network.RetrofitProvider
import com.sleetworks.serenity.android.newone.data.network.safeApiCall
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.ImageRemoteRepository
import okhttp3.ResponseBody
import javax.inject.Inject

class ImageRemoteRepositoryImpl @Inject constructor(
    private val context: Context,
    private val retrofitProvider: RetrofitProvider
) :
    ImageRemoteRepository {

    override suspend fun downloadImageThumbFile(imageId: String): Resource<ResponseBody> {

        return safeApiCall(context) {
            retrofitProvider.getRetrofit().create(ApiService::class.java)
                .downloadImageThumbFile(imageId)
        }

    }

    override suspend fun getImagesForPoint(pointID: String): Resource<ApiResponse<Map<String, String>>> {

        return safeApiCall(
            context
        ) {
            retrofitProvider.getRetrofit().create(ApiService::class.java).getImagesForPoint(pointID)
        }
    }
    override suspend fun getLargeImage(imageID: String): Resource<ResponseBody> {

        return safeApiCall(
            context
        ) {
            retrofitProvider.getRetrofit().create(ApiService::class.java).downloadImageLargeSize(imageID)
        }
    }
}