package com.sleetworks.serenity.android.newone.data.models.local.entities.point

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "point_assignees",
    foreignKeys = [ForeignKey(
        entity = PointEntity::class,
        parentColumns = ["id"],
        childColumns = ["point_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["assignee_id", "point_id"])]
)
data class PointAssigneeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    @ColumnInfo(name = "point_id")
    val pointId: String,
    @ColumnInfo(name = "assignee_id")
    val assigneeId: String,

    )