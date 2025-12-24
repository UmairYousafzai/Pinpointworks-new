package com.sleetworks.serenity.android.newone.domain.usecase

import android.util.Log
import com.sleetworks.serenity.android.newone.data.imageStore.UserImageStore
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.domain.models.point.PointDomain
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.ImageRemoteRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import okhttp3.ResponseBody
import javax.inject.Inject

class SyncPointOriginalImageUseCase @Inject constructor(
    private val imageRepository: ImageRemoteRepository,
    private val userImageStore: UserImageStore
) {
    suspend operator fun invoke(
        workspaceId: String,
        point: PointDomain,
    ) {



        val  originalsToDownload = findMissingImages(point, workspaceId)

        if ( originalsToDownload.isNotEmpty()) {
            coroutineScope {

                val originalJob = async {
                    if (originalsToDownload.isNotEmpty()) downloadOriginals(
                        originalsToDownload,
                        workspaceId
                    )
                }

                originalJob.await()
            }
        }
    }


    private fun findMissingImages(
        point: PointDomain,
        workspaceId: String
    ):  List<String> {
        val originalsToDownload = mutableListOf<String>()

        point.images.forEach { image ->

            val originalFile =
                userImageStore.checkImage(workspaceId, "$workspaceId/images/original/", image.id)
            if (!originalFile.exists()) {
                originalsToDownload.add(image.id)
            }
        }

        return originalsToDownload
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