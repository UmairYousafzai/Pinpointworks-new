package com.sleetworks.serenity.android.newone.domain.usecase

import com.sleetworks.serenity.android.newone.data.network.ApiException
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.domain.reporitories.local.CommentLocalRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.PointRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.local.ReactionLocalRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.CommentRemoteRepository
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.PointRemoteRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject


class SyncPointDetailsUseCase @Inject constructor(
    private val pointRemoteRepository: PointRemoteRepository, // Adjust names to match your project
    private val pointLocalRepository: PointRepository, // Adjust names to match your project
    private val commentRepository: CommentRemoteRepository,
    private val commentLocalRepository: CommentLocalRepository, // If separate
    private val reactionLocalRepository: ReactionLocalRepository
) {
    suspend operator fun invoke(
        pointId: String,
        workspaceId: String,
        siteId: String
    ): Resource<Unit> = coroutineScope {

        // 1. Perform parallel network calls
        val pointDeferred = async { pointRemoteRepository.getPointDetail(pointId, workspaceId) }
        val commentDeferred = async { commentRepository.getPointComments(pointId) }
        val commentReactionDeferred = async { commentRepository.getPointCommentsReaction(pointId) }


        // 2. Await all results
        val pointResult = pointDeferred.await()
        val commentResult = commentDeferred.await()
        val commentReactionResult = commentReactionDeferred.await()

        // 3. Validation Logic
        if (
            pointResult is Resource.Success &&
            commentResult is Resource.Success &&
            commentReactionResult is Resource.Success
        ) {
            val point = pointResult.data.entity
            val comments = commentResult.data.entity
            val reactions = commentReactionResult.data.entity

            if ( point != null && comments != null && reactions != null) {
                pointLocalRepository.insertPoint(point)
                Resource.Success(Unit)
            } else {
                Resource.Error(ApiException.UnknownException("Data entity missing in response"))
            }
        } else {
            Resource.Error(ApiException.UnknownException("One or more network requests failed"))
        }
    }
}
