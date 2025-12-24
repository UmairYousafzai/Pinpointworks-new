package com.sleetworks.serenity.android.newone.data.models.remote.response.comment

import com.google.gson.annotations.SerializedName
import com.sleetworks.serenity.android.newone.data.models.remote.response.Header
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.WorkspaceRef
import java.io.Serializable

data class Comment(
    @SerializedName("_id")
    val id: String,
    val comment: String,
    val commentRich: String,
    val defectRef: DefectRef,
    val header: Header?,
    val tags: List<String>,
    val totalBytes: Int,
    val type: String,
    val workspaceRef: WorkspaceRef,
) : Serializable