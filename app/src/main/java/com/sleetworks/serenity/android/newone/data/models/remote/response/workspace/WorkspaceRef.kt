package com.sleetworks.serenity.android.newone.data.models.remote.response.workspace

import androidx.room.ColumnInfo

data class WorkspaceRef(
    @ColumnInfo(name = "workspace_caption")
    val caption: String?,
    @ColumnInfo(name = "workspace_id")
    val id: String,
    @ColumnInfo(name = "workspace_type")
    val type: String?,

)