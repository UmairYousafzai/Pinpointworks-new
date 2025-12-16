package com.sleetworks.serenity.android.newone.data.repositories.local

import com.sleetworks.serenity.android.newone.data.datasource.local.dao.ReactionDao
import com.sleetworks.serenity.android.newone.data.mappers.toEntity
import com.sleetworks.serenity.android.newone.data.mappers.toModel
import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.Reaction
import com.sleetworks.serenity.android.newone.domain.reporitories.local.ReactionLocalRepository
import javax.inject.Inject

class ReactionLocalRepositoryImpl @Inject constructor(
    val dao: ReactionDao,
) :
    ReactionLocalRepository {
    override suspend fun insertReaction(reaction: Reaction) {
        dao.insertReaction(reaction.toEntity())
    }

    override suspend fun insertReactions(reactions: List<Reaction>) {
        dao.insertReaction(reactions.map { it.toEntity() })
    }

    override suspend fun getReactionsByCommentID(commentId: String): List<Reaction> {
        return dao.getReactionByPointId(commentId)?.map { it.toModel() } ?: emptyList()
    }

    override suspend fun getReactions(): List<Reaction> {
        return dao.getAllReactions().map { it.toModel() }

    }


}