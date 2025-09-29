package com.sleetworks.serenity.android.newone.domain.reporitories.remote

import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.Comment
import com.sleetworks.serenity.android.newone.data.network.Resource

interface CommentRemoteRepository {
    suspend fun getPointComments(
        pointId: String
    ): Resource<ApiResponse<List<Comment>>>

}