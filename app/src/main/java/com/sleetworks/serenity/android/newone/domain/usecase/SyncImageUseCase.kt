package com.sleetworks.serenity.android.newone.domain.usecase

import android.util.Log
import com.sleetworks.serenity.android.newone.data.imageStore.UserImageStore
import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.domain.mapper.toDomain
import com.sleetworks.serenity.android.newone.domain.models.point.PointDomain
import com.sleetworks.serenity.android.newone.domain.models.share.ShareDomainModel
import com.sleetworks.serenity.android.newone.domain.reporitories.local.PointRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.ShareRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.ImageRemoteRepository
import com.sleetworks.serenity.android.newone.presentation.ui.model.PointItemStatus
import com.sleetworks.serenity.android.newone.utils.CONSTANTS.PERMISSION_LIMIT
import com.sleetworks.serenity.android.newone.utils.CONSTANTS.PERMISSION_NORMAL
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.ResponseBody
import javax.inject.Inject

class SyncImageUseCase @Inject constructor(
    private val pointRepository: PointRepository,
    private val shareRepository: ShareRepository,
    private val imageRepository: ImageRemoteRepository,
    private val userImageStore: UserImageStore
) {
    private lateinit var _loader: MutableStateFlow<Pair<String, Boolean>>
    suspend operator fun invoke(workspaceId: String, loader: MutableStateFlow<Pair<String, Boolean>>) {
        this._loader = loader

        // 1. Get all necessary data
        val allPoints = pointRepository.getPointByWorkspaceID(workspaceId).map { it.toDomain() }
        val share = shareRepository.getShareByWorkspaceID(workspaceId)

        // 2. Filter points based on permissions
        val accessiblePoints = filterDefectsByPermission(allPoints, share)

        // 3. Find which images need to be downloaded
        val (thumbnailsToDownload, originalsToDownload) = findMissingImages(accessiblePoints, workspaceId)

        // 4. Execute downloads in parallel
        if (thumbnailsToDownload.isNotEmpty() || originalsToDownload.isNotEmpty()) {

                if (thumbnailsToDownload.isNotEmpty()) downloadThumbnails(thumbnailsToDownload, workspaceId)
                if (originalsToDownload.isNotEmpty()) downloadOriginals(originalsToDownload, workspaceId)



        }
    }

    private fun filterDefectsByPermission(
        list: List<PointDomain>,
        share: ShareDomainModel?,
    ): List<PointDomain> {
        val defectTags = share?.defectTags
        if (share?.shareOption !in listOf(PERMISSION_LIMIT, PERMISSION_NORMAL)) {
            return list
        }

        share ?: return list
        if (defectTags == null || share.tagLimited == false || (!share.canReadTags() && !share.canEditTags())) {
            return list
        }

        val filteredList = when {
            defectTags.isEmpty() -> list.filter { it?.tags?.isEmpty() == true }
            else -> list.filter { defect -> defectTags.any { defect?.tags?.contains(it) == true } }
        }
        return filteredList
    }

    private fun findMissingImages(
        points: List<PointDomain>,
        workspaceId: String
    ): Pair<List<PointDomain>, List<Pair<String, String>>> {
        val thumbnailsToDownload = arrayListOf<PointDomain>() // Use a Set to avoid duplicates
        val originalsToDownload = mutableListOf<Pair<String, String>>()

        points.forEach { point ->
            if (PointItemStatus.from(point.status) in listOf(
                    PointItemStatus.Open,
                    PointItemStatus.InProgress,
                    PointItemStatus.ToReview,
                    PointItemStatus.OnHold
                )
            ) {
                point.images.forEach { image ->
                    val thumbnailFile = userImageStore.checkImage(workspaceId, "$workspaceId/images/thumb/", image.id)
                    if (!thumbnailFile.exists()) {
                        thumbnailsToDownload.add(point) // Add the whole point for thumbnail download
                    }

                    val originalFile = userImageStore.checkImage(workspaceId, "$workspaceId/images/original/", image.id)
                    if (!originalFile.exists()) {
                        originalsToDownload.add(Pair(point.title, image.id))
                    }
                }
            }
        }
        return Pair(thumbnailsToDownload.toList(), originalsToDownload)
    }

    private suspend fun downloadThumbnails(points: List<PointDomain>, workspaceId: String) {
        coroutineScope {
            points.chunked(10).forEach { chunk ->
                chunk.map { point ->
                    _loader.value = Pair("Syncing thumbnails - ${point.title}", true)

                    async {
                        when (val result = imageRepository.getImagesForPoint(point.id)) {
                            is Resource.Success<ApiResponse<Map<String, String>>> -> {
                                result.data.entity?.forEach { (key, value) ->
                                    userImageStore.saveImage(key, value, workspaceId)
                                }
                            }
                            is Resource.Error -> Log.e("SyncImagesUseCase", "Thumbnail download failed for point ${point.id}: ${result.apiException}")
                            else -> {}
                        }
                    }
                }.awaitAll()
            }
        }
    }

    private suspend fun downloadOriginals(images: List<Pair<String, String>>, workspaceId: String) {
        coroutineScope {
            images.chunked(5).forEach { chunk ->
                chunk.map { (pointTitle, imageId) ->
                    _loader.value = Pair("Syncing images - $pointTitle ", true)

                    async {
                        when (val result = imageRepository.getLargeImage(imageId)) {
                            is Resource.Success<ResponseBody> -> {
                                userImageStore.saveLargeImage(imageId, result.data.byteStream(), workspaceId, "$workspaceId/images/original/")
                            }
                            is Resource.Error -> Log.e("SyncImagesUseCase", "Original download failed for image $imageId: ${result.apiException}")
                            else -> {}
                        }
                    }
                }.awaitAll()
            }
        }
    }

}