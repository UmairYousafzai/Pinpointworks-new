package com.sleetworks.serenity.android.newone.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sleetworks.serenity.android.newone.data.models.local.entities.ReactionEntity

@Dao
interface ReactionDao {

    @Upsert
    suspend fun insertReaction(reaction: ReactionEntity)

    @Upsert
    suspend fun insertReaction(reactions: List<ReactionEntity>)

    @Query("SELECT * FROM reaction WHERE comment_id = :commentId")
    suspend fun getReactionByPointId(commentId: String): List<ReactionEntity>?

    @Query("SELECT * FROM reaction")
    suspend fun getAllReactions(): List<ReactionEntity>
}