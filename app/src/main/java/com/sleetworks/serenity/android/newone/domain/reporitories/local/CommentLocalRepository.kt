package com.sleetworks.serenity.android.newone.domain.reporitories.local

import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.Comment

interface CommentLocalRepository {

    suspend fun insertComments(comments: List<Comment>)
    suspend fun getCommentsByPointID(pointID: String): List<Comment>
    suspend fun getComments(): List<Comment>
}