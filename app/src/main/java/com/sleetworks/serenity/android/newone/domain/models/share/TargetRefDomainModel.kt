package com.sleetworks.serenity.android.newone.domain.models.share

import androidx.room.ColumnInfo
import java.io.Serializable

data class TargetRefDomainModel(
    @ColumnInfo(name = "target_ref_type")
    val type: String,
    @ColumnInfo(name = "target_ref_caption")
    val caption: String,
    @ColumnInfo(name = "workspace_id")
    var id: String
): Serializable
