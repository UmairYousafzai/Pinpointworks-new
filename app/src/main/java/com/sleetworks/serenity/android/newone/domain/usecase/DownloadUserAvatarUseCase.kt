package com.sleetworks.serenity.android.newone.domain.usecase

import android.util.Log
import com.sleetworks.serenity.android.newone.data.imageStore.UserImageStore
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.ImageRemoteRepository
import java.io.BufferedInputStream
import javax.inject.Inject

class DownloadUserAvatarUseCase @Inject constructor(
    private val imageRepository: ImageRemoteRepository,
    private val userImageStore: UserImageStore
) {
    private val TAG = "DownloadUsersAvatarUC"

    suspend operator fun invoke(imageID: String) {


        val result = imageRepository.downloadImageThumbFile(imageID)
        when (result) {
            is Resource.Success -> {
                try {
                    val outputStream =
                        userImageStore.getAvatarOutputStream(imageID)
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

}