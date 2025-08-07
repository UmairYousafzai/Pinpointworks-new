package com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share

import androidx.room.ColumnInfo
import java.io.Serializable

data class TargetRef(
    @ColumnInfo(name = "target_ref_type")
    val type: String,
    @ColumnInfo(name = "target_ref_caption")
    val caption: String,
    @ColumnInfo(name = "workspace_id")
    var id: String
): Serializable
