package com.sleetworks.serenity.android.newone.data.models.local

import androidx.room.Embedded
import androidx.room.Relation
import com.sleetworks.serenity.android.newone.data.models.local.entities.CommentEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.ReactionEntity

data class CommentWithReactions(
    @Embedded
    val comment: CommentEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "comment_id",
        entity = ReactionEntity::class
    )
    val reactions: ReactionEntity?
)
