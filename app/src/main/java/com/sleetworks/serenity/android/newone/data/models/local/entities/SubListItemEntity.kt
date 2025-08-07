package com.sleetworks.serenity.android.newone.data.models.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.customfield.SubListItem

@Entity(tableName = "sublist")
data class SubListItemEntity(
    @PrimaryKey()
    val id: Long,
    val label: String="",
    val maxListIndex: Int=0,
    var parentId: String="",
    var fieldParentId: Long=0,
    var workspaceId: String="",
    var isSelected: Boolean=false,
)
