package com.sleetworks.serenity.android.newone.data.models.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assignee")
data class AssigneeEntity(
    @PrimaryKey
    val id: String,
    val caption: String,
    val primaryImageId: String,
    val email: String,
    val type: String
)
