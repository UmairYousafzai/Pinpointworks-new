package com.sleetworks.serenity.android.newone.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sleetworks.serenity.android.newone.data.models.local.entities.CommentEntity

@Dao
interface CommentDao {

    @Upsert
    suspend fun insertComment(comment: CommentEntity)

    @Upsert
    suspend fun insertComments(comments: List<CommentEntity>)

    @Query("SELECT * FROM comments WHERE point_id = :pointID")
    suspend fun getCommentByPointId(pointID: String): List<CommentEntity>?

    @Query("SELECT * FROM comments")
    suspend fun getAllComments(): List<CommentEntity>
}