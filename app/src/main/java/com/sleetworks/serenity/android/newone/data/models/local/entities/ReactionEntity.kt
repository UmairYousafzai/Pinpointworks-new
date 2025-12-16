package com.sleetworks.serenity.android.newone.data.models.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sleetworks.serenity.android.newone.data.models.remote.response.Header

@Entity(tableName = "reaction")
data class ReactionEntity(
    @PrimaryKey
    val id: String,
    val header: Header,
    val like: List<String>,
    @ColumnInfo(name = "comment_id")
    val commentId: String,
    val type: String
)