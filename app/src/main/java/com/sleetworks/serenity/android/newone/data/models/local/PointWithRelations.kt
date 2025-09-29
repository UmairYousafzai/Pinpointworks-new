package com.sleetworks.serenity.android.newone.data.models.local

import androidx.room.Embedded
import androidx.room.Relation
import com.sleetworks.serenity.android.newone.data.models.local.entities.CommentEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.PointCustomFieldEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointAssigneeEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointTagEntity

data class PointWithRelations(
    @Embedded val point: PointEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "point_id",
        entity = PointAssigneeEntity::class
    )
    val assignees: List<PointAssigneeEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "point_id",
        entity = PointTagEntity::class
    )
    val tags: List<PointTagEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "point_id",
        entity = PointCustomFieldEntity::class
    )
    val customFields: List<PointCustomFieldEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "point_id",
        entity = CommentEntity::class
    )
    val comments: List<CommentEntity>,
)