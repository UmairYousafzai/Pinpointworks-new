package com.sleetworks.serenity.android.newone.domain.usecase

import android.util.Log
import com.sleetworks.serenity.android.newone.data.imageStore.UserImageStore
import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.domain.models.point.PointDomain
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.ImageRemoteRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.ResponseBody
import javax.inject.Inject

class SyncPointImageUseCase @Inject constructor(
    private val imageRepository: ImageRemoteRepository,
    private val userImageStore: UserImageStore
) {
    suspend operator fun invoke(
        workspaceId: String,
        point: PointDomain,
        loader: MutableStateFlow<Boolean>
    ) {



        val (thumbnailsToDownload, originalsToDownload) = findMissingImages(point, workspaceId)

        // 4. Execute downloads in parallel
        if (thumbnailsToDownload != null || originalsToDownload.isNotEmpty()) {
            coroutineScope {
                val thumbnailJob = async {
                    if (thumbnailsToDownload != null) downloadThumbnails(
                        thumbnailsToDownload,
                        workspaceId
                    )
                }
                val originalJob = async {
                    if (originalsToDownload.isNotEmpty()) downloadOriginals(
                        originalsToDownload,
                        workspaceId
                    )
                }

                // Wait for both jobs to complete
                thumbnailJob.await()
                originalJob.await()
            }
        }
    }


    private fun findMissingImages(
        point: PointDomain,
        workspaceId: String
    ): Pair<PointDomain?, List<String>> {
        var thumbnailsToDownload: PointDomain? = null // Use a Set to avoid duplicates
        val originalsToDownload = mutableListOf<String>()

        point.images.forEach { image ->
            val thumbnailFile =
                userImageStore.checkImage(workspaceId, "$workspaceId/images/thumb/", image.id)
            if (!thumbnailFile.exists()) {
                thumbnailsToDownload = point
            }

            val originalFile =
                userImageStore.checkImage(workspaceId, "$workspaceId/images/original/", image.id)
            if (!originalFile.exists()) {
                originalsToDownload.add(image.id)
            }
        }

        return Pair(thumbnailsToDownload, originalsToDownload)
    }

    private suspend fun downloadThumbnails(point: PointDomain, workspaceId: String) {
        coroutineScope {

            when (val result = imageRepository.getImagesForPoint(point.id)) {
                is Resource.Success<ApiResponse<Map<String, String>>> -> {
                    result.data.entity?.forEach { (key, value) ->
                        userImageStore.saveImage(key, value, workspaceId)
                    }
                }

                is Resource.Error -> Log.e(
                    "SyncImagesUseCase",
                    "Thumbnail download failed for point ${point.id}: ${result.apiException}"
                )

                else -> {}
            }


        }
    }

    private suspend fun downloadOriginals(images: List<String>, workspaceId: String) {
        coroutineScope {
            images.chunked(5).forEach { chunk ->
                chunk.map {  imageId ->
                    async {
                        when (val result = imageRepository.getLargeImage(imageId)) {
                            is Resource.Success<ResponseBody> -> {
                                userImageStore.saveLargeImage(
                                    imageId,
                                    result.data.byteStream(),
                                    workspaceId,
                                    "$workspaceId/images/original/"
                                )
                            }

                            is Resource.Error -> Log.e(
                                "SyncImagesUseCase",
                                "Original download failed for image $imageId: ${result.apiException}"
                            )

                            else -> {}
                        }
                    }
                }.awaitAll()
            }
        }
    }

}