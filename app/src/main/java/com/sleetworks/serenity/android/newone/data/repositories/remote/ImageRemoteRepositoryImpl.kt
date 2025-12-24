package com.sleetworks.serenity.android.newone.data.repositories.remote

import android.content.Context
import com.sleetworks.serenity.android.newone.data.datasource.remote.ApiService
import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.data.network.RetrofitProvider
import com.sleetworks.serenity.android.newone.data.network.safeApiCall
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.ImageRemoteRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import java.io.File
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
            retrofitProvider.getRetrofit().create(ApiService::class.java)
                .downloadImageLargeSize(imageID)
        }
    }

    override suspend fun removeImage(
        pointId: String,
        imageId: String
    ): Resource<Unit> {

        return safeApiCall(context) {
            retrofitProvider.getRetrofit().create(ApiService::class.java)
                .removeImage(imageId, pointId)
        }
    }

    override suspend fun uploadPointImagesQuick(
        workspaceId: String,
        pointId: String,
        pointCaption:String,
        caption: String,
        exifData: String,
        imageFile: File
    ): Resource<ApiResponse<List<String>>> {
        return safeApiCall(context) {
            val requestFile = imageFile
                .asRequestBody("image/jpeg".toMediaType())

            val imagePart = MultipartBody.Part.createFormData(
                "image_file",
                imageFile.name,
                requestFile
            )

            val exifRequestBody = if (exifData.isNotEmpty()){
                exifData.toRequestBody(
                    "text/plain".toMediaType()
                )
            }else null
            val itemRefIdRequestBody = pointId.toRequestBody(
                "text/plain".toMediaType()
            )

            val itemRefTypeRequestBody = "DefectType".toRequestBody(
                "text/plain".toMediaType()
            )

            val itemRefCaptionRequestBody = pointCaption.toRequestBody(
                "text/plain".toMediaType()
            )

            retrofitProvider.getRetrofit().create(ApiService::class.java)
                .uploadPointImage(
                    exif = exifRequestBody,
                    itemRefId = itemRefIdRequestBody,
                    itemRefType = itemRefTypeRequestBody,
                    itemRefCaption = itemRefCaptionRequestBody,
                    workspaceId = workspaceId,
                    imageFile = imagePart
                )
        }
    }
}