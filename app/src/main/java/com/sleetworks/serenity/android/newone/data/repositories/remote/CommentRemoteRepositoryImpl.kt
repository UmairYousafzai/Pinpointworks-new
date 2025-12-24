package com.sleetworks.serenity.android.newone.data.repositories.remote

import android.content.Context
import com.sleetworks.serenity.android.newone.data.datasource.remote.ApiService
import com.sleetworks.serenity.android.newone.data.models.remote.request.AddCommentRequest
import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.Comment
import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.Reaction
import com.sleetworks.serenity.android.newone.data.network.Resource
import com.sleetworks.serenity.android.newone.data.network.RetrofitProvider
import com.sleetworks.serenity.android.newone.data.network.safeApiCall
import com.sleetworks.serenity.android.newone.domain.reporitories.remote.CommentRemoteRepository
import javax.inject.Inject

class CommentRemoteRepositoryImpl @Inject constructor(
    private val context: Context,
    private val retrofitProvider: RetrofitProvider
) : CommentRemoteRepository {
    override suspend fun addComment(
        pointId: String,
        commentRequest: AddCommentRequest
    ): Resource<ApiResponse<Comment>> {

        return safeApiCall(context){
            retrofitProvider.getRetrofit().create(ApiService::class.java).addComment(pointId,commentRequest)
        }
    }


    override suspend fun getPointComments(pointId: String): Resource<ApiResponse<List<Comment>>> {

        return safeApiCall(context) {
            retrofitProvider.getRetrofit().create(ApiService::class.java)
                .getPointComment(pointId)
        }
    }

    override suspend fun getPointCommentsReaction(pointId: String): Resource<ApiResponse<List<Reaction>>> {
        return safeApiCall(context) {
            retrofitProvider.getRetrofit().create(ApiService::class.java)
                .getPointCommentReactions(pointId)
        }
    }

    override suspend fun updateCommentReaction(
        commentId: String,
        remove: Boolean
    ): Resource<ApiResponse<Reaction>> {
        return safeApiCall(context) {
            retrofitProvider.getRetrofit().create(ApiService::class.java)
                .addReaction(commentId = commentId, remove = remove)
        }
    }


}