package com.sleetworks.serenity.android.newone.domain.usecase

import android.util.Log
import com.sleetworks.serenity.android.newone.data.imageStore.UserImageStore
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.domain.reporitories.local.UserRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.ImageRemoteRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.BufferedInputStream
import javax.inject.Inject

class DownloadUsersAvatarUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val imageRepository: ImageRemoteRepository,
    private val userImageStore: UserImageStore
) {
    private val TAG = "DownloadUsersAvatarUC"

    suspend operator fun invoke(workspaceId: String, authUserId: String) {
        coroutineScope {
            val assignees = userRepository.getUserByWorkspaceId(workspaceId)
            val users = assignees.filter {
                authUserId != it.id && userImageStore.getAvatarFile(it.primaryImageId) == null

            }
            users.chunked(10).forEach { chunk ->

                chunk.map { assignee ->
                    async {
                        val result = imageRepository.downloadImageThumbFile(assignee.primaryImageId)
                        when (result) {
                            is Resource.Success -> {
                                try {
                                    val outputStream =
                                        userImageStore.getAvatarOutputStream(assignee.primaryImageId)
                                    val inputStream = BufferedInputStream(result.data.byteStream())
                                    var b: Int
                                    while ((inputStream.read().also { b = it }) != -1) {
                                        outputStream.write(b)
                                    }
                                    outputStream.flush()
                                } catch (e: Exception) {
                                    Log.e(TAG, "downloadUserAvatar: ", e)
                                }
                            }


                            is Resource.Error -> {
                                Log.e(TAG, "downloadUserAvatar: ", result.apiException)
                            }

                            Resource.Loading -> {}
                        }
                    }
                }.awaitAll()

            }

        }
    }

}