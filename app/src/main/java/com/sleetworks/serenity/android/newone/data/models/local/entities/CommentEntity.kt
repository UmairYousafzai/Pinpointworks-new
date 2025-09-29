package com.sleetworks.serenity.android.newone.data.models.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.Header
import com.sleetworks.serenity.android.newone.data.models.remote.response.comment.DefectRef
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.WorkspaceRef

@Entity(
    tableName = "comments",
    foreignKeys = [ForeignKey(
        entity = PointEntity::class,
        parentColumns = ["id"],
        childColumns = ["point_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],

    indices = [Index(value = ["workspace_id", "point_id"])]

)
data class CommentEntity(
    @PrimaryKey
    val id: String,
    val comment: String,
    val commentRich: String,
    @Embedded
    val defectRef: DefectRef,
    val header: Header,
    val tags: List<String>,
    val totalBytes: Int,
    val type: String,
    @Embedded
    val workspaceRef: WorkspaceRef
)