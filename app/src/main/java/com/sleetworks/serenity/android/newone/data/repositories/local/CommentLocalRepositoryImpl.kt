package com.sleetworks.serenity.android.newone.data.repositories.local

import com.sleetworks.serenity.android.newone.data.datasource.local.dao.CommentDao
import com.sleetworks.serenity.android.newone.data.mappers.toEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.CommentEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.Comment
import com.sleetworks.serenity.android.newone.domain.mapper.toDomain
import com.sleetworks.serenity.android.newone.domain.reporitories.local.CommentLocalRepository
import javax.inject.Inject

class CommentLocalRepositoryImpl @Inject constructor(
    val commentDao: CommentDao,
) :
    CommentLocalRepository {
    override suspend fun insertComment(comment: CommentEntity) {
        commentDao.insertComment(comment)
    }


    override suspend fun insertComments(comments: List<Comment>) {
        val commentsEntity = comments.map { it.toEntity() }
        commentDao.insertComments(commentsEntity)
    }

    override suspend fun getCommentsByPointID(pointID: String): List<Comment> {
        val comments = commentDao.getCommentByPointId(pointID)
        val commentsDomain = comments?.map { it.toDomain() } ?: emptyList()
        return commentsDomain
    }

    override suspend fun getComments(): List<Comment> {
        val comments = commentDao.getAllComments()
        val commentsDomain = comments.map { it.toDomain() }
        return commentsDomain
    }


}