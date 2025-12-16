package com.sleetworks.serenity.android.newone.domain.reporitories.local

import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.Reaction

interface ReactionLocalRepository {

    suspend fun insertReaction(reaction: Reaction)
    suspend fun insertReactions(reactions: List<Reaction>)
    suspend fun getReactionsByCommentID(commentId: String): List<Reaction>
    suspend fun getReactions(): List<Reaction>
}