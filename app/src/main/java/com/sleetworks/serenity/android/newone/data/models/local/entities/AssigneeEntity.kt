package com.sleetworks.serenity.android.newone.data.models.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assignee")
data class AssigneeEntity(
    @PrimaryKey
    val id: String,
    val caption: String,
    @ColumnInfo(name = "primary_image_id")
    val primaryImageId: String,
    val email: String,
    val type: String,
    @ColumnInfo(name = "workspace_id")
    val workspaceId: String
)
